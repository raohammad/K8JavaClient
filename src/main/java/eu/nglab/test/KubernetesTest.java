package eu.nglab.test;

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class KubernetesTest {

    public static void main(String[] args) throws IOException, ApiException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String podmanifest = classloader.getResource("test-svc.yaml").getPath();
        String deploymanifest = classloader.getResource("test-deployment.yaml").getPath();

        String kubeConfig = "/var/snap/microk8s/current/credentials/client.config";
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfig))).build();
        crudDeployment(client, deploymanifest);

        //ApiClient client = Config.defaultClient();
        /* Get All Kubernetes PODs */
//        Configuration.setDefaultApiClient(client);
//
//        CoreV1Api api = new CoreV1Api();
//        V1PodList list =
//                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
//        for (V1Pod item : list.getItems()) {
//            System.out.println(item.getMetadata().getName());
//        }
    }

    static void crudPod(ApiClient cl, String manifest) throws IOException, ApiException {
        V1Pod pod =
                new V1PodBuilder()
                        .withNewMetadata()
                        .withName("apod")
                        .endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName("www")
                        .withImage("nginx")
                        .withNewResources()
                        .withLimits(new HashMap<>())
                        .endResources()
                        .endContainer()
                        .endSpec()
                        .build();
        System.out.println(Yaml.dump(pod));

        // Read yaml configuration file, and deploy it
        ApiClient client = cl;
        Configuration.setDefaultApiClient(client);

        //  See issue #474. Not needed at most cases, but it is needed if you are using war
        //  packging or running this on JUnit.
        Yaml.addModelMap("v1", "Service", V1Service.class);

        // Example yaml file can be found in $REPO_DIR/test-svc.yaml
        File file = new File(manifest);
        V1Service yamlSvc = (V1Service) Yaml.load(file);

        // Deployment and StatefulSet is defined in apps/v1, so you should use AppsV1Api instead of
        // CoreV1API
        CoreV1Api api = new CoreV1Api();
//        V1Pod creatPodResult = api.createNamespacedPod("default", pod, null, null, null);
//        System.out.println(creatPodResult);

        V1Status deleteResult =
                api.deleteNamespacedPod(
                        pod.getMetadata().getName(),
                        "default",
                        null,
                        null,
                        null,
                        null,
                        null,
                        new V1DeleteOptions());
        System.out.println(deleteResult);
    }

    //Service read from
    static void crudService(ApiClient cl, String manifest) throws IOException, ApiException {

//        V1Service svc =
//                new V1ServiceBuilder()
//                        .withNewMetadata()
//                        .withName("aservice")
//                        .endMetadata()
//                        .withNewSpec()
//                        .withSessionAffinity("ClientIP")
//                        .withType("NodePort")
//                        .addNewPort()
//                        .withProtocol("TCP")
//                        .withName("client")
//                        .withPort(8008)
//                        .withNodePort(8080)
//                        .withTargetPort(new IntOrString(8080))
//                        .endPort()
//                        .endSpec()
//                        .build();
//        System.out.println(Yaml.dump(svc));

        // Read yaml configuration file, and deploy it
        ApiClient client = cl;
        Configuration.setDefaultApiClient(client);

        //  See issue #474. Not needed at most cases, but it is needed if you are using war
        //  packging or running this on JUnit.
        Yaml.addModelMap("v1", "Service", V1Service.class);

        // Example yaml file can be found in $REPO_DIR/test-svc.yaml
        File file = new File(manifest);
        V1Service yamlSvc = (V1Service) Yaml.load(file);

        // Deployment and StatefulSet is defined in apps/v1, so you should use AppsV1Api instead of
        // CoreV1API
        CoreV1Api api = new CoreV1Api();

        V1Service createResult = api.createNamespacedService("default", yamlSvc, null, null, null);
        System.out.println(createResult);

        V1Status deleteResult =
                api.deleteNamespacedService(
                        yamlSvc.getMetadata().getName(),
                        "default",
                        null,
                        null,
                        null,
                        null,
                        null,
                        new V1DeleteOptions());
        System.out.println(deleteResult);
    }

    static void crudDeployment(ApiClient cl, String manifest) throws IOException, ApiException {

        // Read yaml configuration file, and deploy it
        ApiClient client = cl;
        Configuration.setDefaultApiClient(client);

        //  See issue #474. Not needed at most cases, but it is needed if you are using war
        //  packging or running this on JUnit.
        Yaml.addModelMap("v1", "Service", V1Service.class);

        // Example yaml file can be found in $REPO_DIR/test-svc.yaml
        File file = new File(manifest);
        V1Deployment yamlDeploiment = (V1Deployment) Yaml.load(file);
        V1ObjectMeta objectMeta = yamlDeploiment.getMetadata();
        objectMeta.setName("Hello-Ass-Name As");
        System.out.println(yamlDeploiment);

        // Deployment and StatefulSet is defined in apps/v1, so you should use AppsV1Api instead of
        // CoreV1API
        AppsV1Api api = new AppsV1Api();
//        V1Pod creatPodResult = api.createNamespacedPod("default", pod, null, null, null);
//        System.out.println(creatPodResult);

        V1Deployment createPodTemplateResult = api.createNamespacedDeployment("default", yamlDeploiment, null, null, null);
        System.out.println(createPodTemplateResult);

//        V1Status deleteResult =
//                api.deleteNamespacedDeployment(
//                        yamlDeploiment.getMetadata().getName(),
//                        "default",
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        new V1DeleteOptions());
//        System.out.println(deleteResult);
    }
}
