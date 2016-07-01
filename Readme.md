1) Change the AJAX Servlet resourceBase="${app.home}/docroot" to resourceBase="/Users/vijay.polsani/app/apache-tomcat-muleservice-7.0.52/webapps/li-client/docroot"
<br>
2) Change the AJAX Servlet serverUrl="http://0.0.0.0:8090/li-client/services/result" to serverUrl="http://0.0.0.0:8090/<DEPLOYMENT_NAME>/services/result"
<br>
3) Make sure to change the POM file from mule deployment to WAR deployment.
<br>
4) Make sure to relpace the variable information.
<Lithium:config name="Lithium" lithiumPassword="${lithium.password}" lithiumUserName="${lithium.username}"  communityName = "${community.name}" doc:name="Lithium" communityHostname="${community.hostname}" >
TO:
<Lithium:config name="Lithium" lithiumPassword="arfarf" lithiumUserName="admin"   doc:name="Lithium" 
<br>
5) Change  <http:endpoint exchange-pattern="request-response" host="localhost" port="${port}" path="lithium" method="POST" name="HTTP" doc:name="HTTP"/> -->
to <http:inbound-endpoint exchange-pattern="one-way"  ref="HTTP" doc:name="HTTP 8081/lithium"  transformer-refs="Body_to_Parameter_Map" contentType="text/html"/>
<br>
6) Comment the servelet as it may be interceting the path.
<br>
7) Rename the localhost of the index.html in the forwarding page http://localhost:8081/lithium TO http://vijay-polsani.vm.lithium.com:8081/lithium
a) Launch the http://localhost:8080/li-client
Hit Submit. (Check if the forward address is http://localhost:8081/lithium)
b) Check the URL for updates: http://localhost:8090/li-client/services/result/
