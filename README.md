### Camel Archetypes

    mvn archetype:generate -DarchetypeGroupId=org.apache.camel.archetypes -DarchetypeArtifactId=camel-archetype-spring-boot -DarchetypeVersion=3.11.0
	
### Configuring the Application

    oc login https://api.shared-na46.openshift.opentlc.com:6443/ -u username
	oc new-project username-camel
	oc new-app java:8~https://github.com/username/camel-workshop.git
	oc expose service/camel-workshop
	oc patch route camel-workshop -p '{"spec":{"tls": {"insecureEdgeTerminationPolicy":"None", "termination":"edge"}}}'
	
	
### Incremental builds (if supported)

    strategy:
      sourceStrategy:
        from:
          kind: "ImageStreamTag"
          name: "incremental-image:latest" 
        incremental: true
	
### Github Webhooks

    oc get bc -o yaml copy the secret
	oc describe bc get webhook url
	
### Creating service accounts

    oc create sa robot
    oc policy add-role-to-user edit system:serviceaccount:[namespace]:robot

example

	oc policy add-role-to-user edit system:serviceaccount:my-project:robot
	oc describe secret robot-token-2w64j
		
### creating liveness and readiness probes

    oc set probe deployment/camel-workshop --readiness --get-url=http://:8080/actuator/health/ --initial-delay-seconds=60
	oc set probe deployment/camel-workshop --liveness --get-url=http://:8080/actuator/health/ --initial-delay-seconds=60

### setting limits
    
    oc set resources deployment camel-workshop --limits=cpu=1,memory=256Mi

### creating secrets
   
    oc create secret --help

### setting environment variables and secrets

    oc set env --help


### setting persistent volumes

    oc set volume --help


### Testing this App

For testing this App you can use the tester.py application and deploy using the new-app command:

     oc new-app --name=camel-tester https://github.com/jorgecastro05/camel-workshop.git --context-dir=testerApp

This create a new app with name camel-tester and builds the aplication with Dockerfile inside the folder.
Then you can expose the new app created

    oc expose svc camel-tester

And navigate with a web browser at url created, this exposes a swagger UI for test the application, where you can use the openshift url, application name, and token for accesing the cluster.

#### Set timeout for Service in case of errors with timeouts

    oc annotate route camel-tester --overwrite haproxy.router.openshift.io/timeout=5m