

# StateResponse


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**phase** | [**PhaseEnum**](#PhaseEnum) |  |  |
|**myTurn** | **Boolean** |  |  |
|**iAmWinner** | **Boolean** |  |  |
|**board** | **List&lt;List&lt;String&gt;&gt;** |  |  |
|**names** | [**Names**](Names.md) |  |  |
|**iamWinner** | **Boolean** |  |  [optional] |



## Enum: PhaseEnum

| Name | Value |
|---- | -----|
| WAITING_FOR_PLAYERS | &quot;WAITING_FOR_PLAYERS&quot; |
| ONGOING_GAME | &quot;ONGOING_GAME&quot; |
| PLAYER_DISCONNECTED | &quot;PLAYER_DISCONNECTED&quot; |
| PLAYER_WON | &quot;PLAYER_WON&quot; |



