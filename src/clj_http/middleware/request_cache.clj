(ns clj-http.middleware.request-cache
  (:require [clj-http.client :as http]
            [clj-http.headers :as headers]
            [clojure.core.cache :as cache])
  (:import (org.apache.commons.io IOUtils)))

(defn- default-cache-key [{:keys [server-name uri query-string]}]
  (str server-name uri (.hashCode (str "?" query-string))))

(defn wrap-request-cache
  [client cache-instance & {:keys [cache-key debug?]
                            :or {cache-key default-cache-key
                                 debug? true}}]
  (fn [request]
    (let [k (cache-key request)]
      (if (cache/has? @cache-instance k)
        (do (when debug? (println "Cache hit:" k))
            (swap! cache-instance cache/hit k)
            (update (cache/lookup @cache-instance k)
                    :headers
                    (partial into (headers/header-map))))
        (do (when debug? (println "Cache miss:" k))
            (let [response (client request)]
              (when (and (http/success? response)
                         (#{:get} (:request-method request)))
                (swap! cache-instance
                       cache/miss
                       k
                       (update (client request)
                               :body #(IOUtils/toByteArray %))))
              response))))))
