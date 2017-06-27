(ns clj-http.middleware.request-cache-test
  (:require [clojure.test :refer :all]
            [clj-http.middleware.request-cache :refer :all]
            [clj-http.client :as http]
            [clojure.core.cache :as cache]))

(deftest wrap-request-cache-test
  (testing "it caches GET requests"
    (let [http-cache (atom (cache/basic-cache-factory {}))]
      (http/with-additional-middleware [#(wrap-request-cache % http-cache)]
        (http/get "http://www.example.com?asasd")
        (is (= 1 (count (keys @http-cache))))

        (http/post "http://www.example.com?dddd")
        (is (= 1 (count (keys @http-cache))))))))
