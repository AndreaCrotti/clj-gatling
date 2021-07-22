(ns metrics-simulation.core
  (:require [clj-gatling.core :as gatling]
            [metrics-simulation.simulations :refer [simulations]])
  (:import (java.time Duration))
  (:gen-class))

(defn- ramp-up-distribution [percentage-at _]
  (cond
    (< percentage-at 0.1) 0.1
    (< percentage-at 0.2) 0.2
    :else 1.0))

(defn -main [simulation users requests-or-duration & [option]]
  (let [simulation (or ((keyword simulation) simulations)
                       (throw (Exception. (str "No such simulation " simulation))))]
    (condp = option
      "--with-duration" (gatling/run simulation
                                 {:concurrency (read-string users)
                                  :concurrency-distribution ramp-up-distribution
                                  :duration (Duration/ofSeconds (read-string requests-or-duration))})
      "--no-report" (gatling/run simulation
                                 {:concurrency (read-string users)
                                  :concurrency-distribution ramp-up-distribution
                                  :reporter {:writer (fn [_ _ _])
                                             :generator (fn [simulation]
                                                          (println "Ran" simulation "without report"))}
                                  :requests (read-string requests-or-duration)})
      "--ramp-up" (gatling/run simulation
                               {:concurrency (read-string users)
                                :concurrency-distribution ramp-up-distribution
                                :root "tmp"
                                :requests (read-string requests-or-duration)})
      (gatling/run simulation
                   {:concurrency (read-string users)
                    :root "tmp"
                    :requests (read-string requests-or-duration)}))))
