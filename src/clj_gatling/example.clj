(ns clj-gatling.example)

(defn run-request [id]
  ;(println (str "Simulating request for " id))
  (Thread/sleep (rand 1000))
  "OK")

(def test-scenario
  {:name "Test scenario"
   :requests [{:name "Request1" :fn run-request}
              {:name "Request2" :fn run-request}]})