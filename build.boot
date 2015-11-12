(set-env!
  :source-paths #{"src"}
  :resource-paths #{"src"}
  :dependencies '[[adzerk/bootlaces "0.1.13"]
                  [org.clojure/clojure "1.7.0"]])

(def +version+ "0.1.0-SNAPSHOT")

(task-options!
  pom {:project 'mecha1/boot-prerender
       :version +version+})

(require '[adzerk.bootlaces :refer :all])

(bootlaces! +version+)