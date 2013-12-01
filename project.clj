(defproject wall-follower "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :uberjar-name "ev3main.jar"
  :main wall_follower.Main
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :aliases {"run-ev3" ["do" "uberjar,"
                       "shell" "scp" "target/ev3main.jar" "root@10.0.1.1:,"
                       "shell" "ssh" "root@10.0.1.1" "/bin/jrun -jar /home/root/ev3main.jar"]
            "shutdown-ev3" ["shell" "ssh" "root@10.0.1.1" "halt"]}
  :dependencies [[org.clojars.earthlingzephyr/lejos-ev3 "0.5.0-SNAPSHOT"]]
  :plugins [[lein-shell "0.3.0"]])
