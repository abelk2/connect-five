# GameBackendApi

All URIs are relative to *http://localhost:54433*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**disconnect**](GameBackendApi.md#disconnect) | **POST** /disconnect |  |
| [**disconnectWithHttpInfo**](GameBackendApi.md#disconnectWithHttpInfo) | **POST** /disconnect |  |
| [**getState**](GameBackendApi.md#getState) | **GET** /state |  |
| [**getStateWithHttpInfo**](GameBackendApi.md#getStateWithHttpInfo) | **GET** /state |  |
| [**join**](GameBackendApi.md#join) | **POST** /join |  |
| [**joinWithHttpInfo**](GameBackendApi.md#joinWithHttpInfo) | **POST** /join |  |
| [**step**](GameBackendApi.md#step) | **POST** /step |  |
| [**stepWithHttpInfo**](GameBackendApi.md#stepWithHttpInfo) | **POST** /step |  |



## disconnect

> void disconnect(disconnectRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        DisconnectRequest disconnectRequest = new DisconnectRequest(); // DisconnectRequest | 
        try {
            apiInstance.disconnect(disconnectRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#disconnect");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **disconnectRequest** | [**DisconnectRequest**](DisconnectRequest.md)|  | |

### Return type


null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

## disconnectWithHttpInfo

> ApiResponse<Void> disconnect disconnectWithHttpInfo(disconnectRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.ApiResponse;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        DisconnectRequest disconnectRequest = new DisconnectRequest(); // DisconnectRequest | 
        try {
            ApiResponse<Void> response = apiInstance.disconnectWithHttpInfo(disconnectRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#disconnect");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **disconnectRequest** | [**DisconnectRequest**](DisconnectRequest.md)|  | |

### Return type


ApiResponse<Void>

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## getState

> StateResponse getState(stateRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        StateRequest stateRequest = new StateRequest(); // StateRequest | 
        try {
            StateResponse result = apiInstance.getState(stateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#getState");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **stateRequest** | [**StateRequest**](.md)|  | |

### Return type

[**StateResponse**](StateResponse.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

## getStateWithHttpInfo

> ApiResponse<StateResponse> getState getStateWithHttpInfo(stateRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.ApiResponse;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        StateRequest stateRequest = new StateRequest(); // StateRequest | 
        try {
            ApiResponse<StateResponse> response = apiInstance.getStateWithHttpInfo(stateRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#getState");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **stateRequest** | [**StateRequest**](.md)|  | |

### Return type

ApiResponse<[**StateResponse**](StateResponse.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## join

> JoinResponse join(joinRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        JoinRequest joinRequest = new JoinRequest(); // JoinRequest | 
        try {
            JoinResponse result = apiInstance.join(joinRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#join");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **joinRequest** | [**JoinRequest**](JoinRequest.md)|  | |

### Return type

[**JoinResponse**](JoinResponse.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

## joinWithHttpInfo

> ApiResponse<JoinResponse> join joinWithHttpInfo(joinRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.ApiResponse;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        JoinRequest joinRequest = new JoinRequest(); // JoinRequest | 
        try {
            ApiResponse<JoinResponse> response = apiInstance.joinWithHttpInfo(joinRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#join");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **joinRequest** | [**JoinRequest**](JoinRequest.md)|  | |

### Return type

ApiResponse<[**JoinResponse**](JoinResponse.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## step

> void step(stepRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        StepRequest stepRequest = new StepRequest(); // StepRequest | 
        try {
            apiInstance.step(stepRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#step");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **stepRequest** | [**StepRequest**](StepRequest.md)|  | |

### Return type


null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

## stepWithHttpInfo

> ApiResponse<Void> step stepWithHttpInfo(stepRequest)



### Example

```java
// Import classes:
import eu.abelk.connectfive.ApiClient;
import eu.abelk.connectfive.ApiException;
import eu.abelk.connectfive.ApiResponse;
import eu.abelk.connectfive.Configuration;
import eu.abelk.connectfive.models.*;
import eu.abelk.connectfive.api.GameBackendApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:54433");

        GameBackendApi apiInstance = new GameBackendApi(defaultClient);
        StepRequest stepRequest = new StepRequest(); // StepRequest | 
        try {
            ApiResponse<Void> response = apiInstance.stepWithHttpInfo(stepRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
        } catch (ApiException e) {
            System.err.println("Exception when calling GameBackendApi#step");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **stepRequest** | [**StepRequest**](StepRequest.md)|  | |

### Return type


ApiResponse<Void>

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

