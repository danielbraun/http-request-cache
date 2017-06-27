# http-request-cache

clj-http middleware to cache successful GET requests.

## Usage

```

(require '[clj-http.middleware.request-cache :refer [wrap-request-cache]]                                                                                     
         '[clj-http.client :as http]                                                                                                                          
         '[clojure.core.cache :as cache])

(def http-cache (atom (cache/basic-cache-factory {})))

(http/with-additional-middleware [#(wrap-request-cache % http-cache)]
  (http/get "http://www.example.com") ; Cache miss, request is now cached
  (http/get "http://www.example.com") ; Cache hit, instant response.
  )


```

## License

Copyright © 2017 Daniel Braun

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
