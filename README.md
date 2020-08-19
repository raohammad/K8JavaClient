# Introduction

Repository that contains usecases of developing K8 with official java sdk

## Useful commands

    microk8s kubectl get pods --all-namespaces

Start dashboard proxy

    microk8s dashboard-proxy

Get secrets in cluster

    microk8s kubectl get secrets --all-namespaces

check for namespace: default   name: default-token-xxxxx for getting token of dashboard

    microk8s kubectl describe secret default-token-xxxxx -n default

Deploy a manifest (if private registry contained, please see NOTE section below) 

    microk8s kubectl apply -f deployment-manifest.yaml

NOTE: 
1st Method:
  
Pass username/password and create a secret

    kubectl create secret docker-registry regcredcl --docker-server=<your-registry-server> --docker-username=<your-name> --docker-password=<your-pword> --docker-email=<your-email>

2nd Method:
In order to pull from private registry, its login credentials need to be created in     
    
    docker login registry.gitlab.com
    <Login Succeeded>
    
Node credentials are generated in docker's config.json

    cat ~/.docker/config.json
    
With below command, create a secret inside k8

    microk8s kubectl create secret generic regcred --from-file=.dockerconfigjson=/home/hmad/.docker/config.json
where regred is name of the secret

Check if secret exists;

     microk8s kubectl get secret regcredcl --output=yaml

Add Registry credentials created to YAML file of your deployment.
