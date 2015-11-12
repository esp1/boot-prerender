(ns mecha1.boot-prerender
  (:require [boot.core :as boot]
            [boot.util :as util]
            [clojure.java.io :as io]))

(def ^:private renderjs
  "var sys  = require('system');
  var uri = sys.args[1];

  var page = require('webpage').create();
  page.open(uri, function(status) {
    setTimeout(function() {
      var html = page.evaluate(function() {
        return document.documentElement.outerHTML;
      });
      console.log(html);
      phantom.exit();
    }, 0);
  });")

(boot/deftask phantom
  "Prerenders all .html output files in the fileset by using phantomjs."
  []
  (let [rjs-dir  (boot/tmp-dir!)
        rjs-path (.getPath (io/file rjs-dir "render.js"))
        out-dir  (boot/tmp-dir!)]
    (spit rjs-path renderjs)
    (boot/with-pre-wrap fileset
      (doseq [h (->> fileset
                  boot/output-files
                  (boot/by-ext [".html"]))
              :let [html-in-abs-path (.getPath (boot/tmp-file h))
                    html-out-f       (io/file out-dir (boot/tmp-path h))]]
        (spit html-out-f (:out (clojure.java.shell/sh "phantomjs" rjs-path html-in-abs-path))))
      (-> fileset
        (boot/add-resource out-dir)
        boot/commit!))))
