# xiot-service-specification
IoT规范服务

版本: 0.2



| 时间      | 修改者                     | 描述                             |
| --------- | -------------------------- | ---------------------- |
| 2020.4.28 | shenweijie@knowin.com | 初始化文档 |
| 2020.11.13 | shenweijie@knowin.com | 增加根据DeviceType读取实例状态接口 |


 ### 域名

| 域名                     | 环境     |
| ------------------------ | -------- |
| dev-iot-spec.knowin.com | 开发环境 |
| st-iot-spec.knowin.com  | 测试环境 |
| pv-iot-spec.knowin.com  | 预览环境 |
| iot-spec.knowin.com     | 正式环境 |

 ### 一、产品规范

 ####  1.1 名字空间

 ##### 1.1.1 读取所有名字空间

 * 请求

   ```http
   GET /namespaces
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "namespaces": [
               {
                   "namespace": "knowin-spec",
                   "description": {
                       "zh-CN": "如影设备规范",
                       "zh-TW": "如影设备规范",
                       "en-US": "knowin device specificiation"
                   }
               },
               {
                   "namespace": "homekit-spec",
                   "description": {
                       "zh-CN": "苹果配件规范",
                       "zh-TW": "苹果配件规范",
                       "en-US": "apple accessory specificiation"
                   }
               }
           ]
       }
   }
   ```

 ##### 1.1.2 读取单个名字空间

 * 请求

   ```http
   GET /namespace?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "namespace": "knowin-spec",
           "description": {
               "zh-CN": "如影设备规范",
               "zh-TW": "如影设备规范",
               "en-US": "knowin device specificiation"
           }
       }
   }
   ```

 ####  1.2 设备

 ##### 1.2.1 读取所有设备种类

 * 请求

   ```http
   GET /devices
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "devices": [
               {
                   "namespace": "knowin-spec",
                   "name": "lightbulb",
                   "description": {
                       "zh-CN": "灯泡",
                       "zh-TW": "灯泡",
                       "en-US": "lightbulb"
                   }
               },
               {
                   "namespace": "homekit-spec",
                   "name": "switch",
                   "description": {
                       "zh-CN": "开关",
                       "zh-TW": "开关",
                       "en-US": "switch"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.2.2 读取某个名字空间下的所有设备种类

 * 请求

   ```http
   GET /devices?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "devices": [
               {
                   "namespace": "knowin-spec",
                   "name": "lightbulb",
                   "description": {
                       "zh-CN": "灯泡",
                       "zh-TW": "灯泡",
                       "en-US": "lightbulb"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.2.3 读取某个名字空间下的单个设备种类

 * 请求

   ```http
   GET /device?ns=knowin-spec&name=lightbulb
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "name": "lightbulb",
           "description": {
               "zh-CN": "灯泡",
               "zh-TW": "灯泡",
               "en-US": "lightbulb"
           }
       }
   }
   ```
   
 ####  1.3 服务

 ##### 1.3.1 读取所有服务

 * 请求

   ```http
   GET /services
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "services": [
               {
                   "namespace": "knowin-spec",
                   "name": "lightbulb",
                   "description": {
                       "zh-CN": "灯泡",
                       "zh-TW": "灯泡",
                       "en-US": "lightbulb"
                   }
               },
               {
                   "namespace": "homekit-spec",
                   "name": "switch",
                   "description": {
                       "zh-CN": "开关",
                       "zh-TW": "开关",
                       "en-US": "switch"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.3.2 读取某个名字空间下的所有服务

 * 请求

   ```http
   GET /services?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "services": [
               {
                   "namespace": "knowin-spec",
                   "name": "lightbulb",
                   "description": {
                       "zh-CN": "灯泡",
                       "zh-TW": "灯泡",
                       "en-US": "lightbulb"
                   }
               },
               {
                   "namespace": "knowin-spec",
                   "name": "switch",
                   "description": {
                       "zh-CN": "开关",
                       "zh-TW": "开关",
                       "en-US": "switch"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.3.3 读取某个名字空间下的单个服务定义

 * 请求（指定名字空间和名称）

   ```http
   GET /service?ns=knowin-spec&name=lightbulb
   ```

 * 或者（指定type）

   ```http
   GET /service?type=urn:knowin-spec:service:lightbulb:00000001
   ```
   
 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "type": "urn:knowin-spec:service:lightbulb:00000001",
           "description": {
               "zh-CN": "灯泡",
               "zh-TW": "灯泡",
               "en-US": "lightbulb"
           },
           "required-properties": [
               "urn:knowin-spec:property:on:00001001"
           ],
           "optional-properties": [
               "urn:knowin-spec:property:xxx:00001002"
           ]
       }
   }
   ```
   
 #### 1.4 属性

 ##### 1.4.1 读取所有属性

 * 请求

   ```http
   GET /properties
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "properties": [
               {
                   "namespace": "knowin-spec",
                   "name": "on",
                   "description": {
                       "zh-CN": "开关",
                       "zh-TW": "开关",
                       "en-US": "on"
                   }
               },
               {
                   "namespace": "homekit-spec",
                   "name": "brightness",
                   "description": {
                       "zh-CN": "亮度",
                       "zh-TW": "亮度",
                       "en-US": "brightness"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.4.2 读取某个名字空间下的所有属性

 * 请求

   ```http
   GET /properties?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "properties": [
               {
                   "namespace": "knowin-spec",
                   "name": "on",
                   "description": {
                       "zh-CN": "开关",
                       "zh-TW": "开关",
                       "en-US": "on"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.4.3 读取某个名字空间下的单个属性定义

 * 请求（指定名字空间和名称）

   ```http
   GET /property?ns=knowin-spec&name=on
   ```

 * 或者（指定type）

   ```http
   GET /property?type=urn:knowin-spec:property:on:00000017
   ```
   
 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "type": "urn:knowin-spec:property:on:00000017",
           "description": {
               "zh-CN": "通用开关",
               "zh-TW": "通用开关",
               "en-US": "onoff"
           },
           "format": "boolean",
           "access": ["read", "write", "notify"]
       }
   }
   ```
   
 #### 1.5 方法

 ##### 1.5.1 读取所有方法

 * 请求

   ```http
   GET /actions
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "properties": [
               {
                   "namespace": "knowin-spec",
                   "name": "play",
                   "description": {
                       "zh-CN": "播放",
                       "zh-TW": "播放",
                       "en-US": "play"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.5.2 读取某个名字空间下的所有方法

 * 请求

   ```http
   GET /actions?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "properties": [
               {
                   "namespace": "knowin-spec",
                   "name": "play",
                   "description": {
                       "zh-CN": "播放",
                       "zh-TW": "播放",
                       "en-US": "play"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.5.3 读取某个名字空间下的单个方法定义

 * 请求（指定名字空间和名称）

   ```http
   GET /action?ns=knowin-spec&name=play
   ```

 * 或者（指定type）

   ```http
   GET /action?type=urn:knowin-spec:action:play:00000001
   ```
   
 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "type": "urn:knowin-spec:action:play:00000001",
           "description": {
               "zh-CN": "播放",
               "zh-TW": "播放",
               "en-US": "play"
           },
           "in": [
               "urn:knowin-spec:property:a:00001001",
               "urn:knowin-spec:property:b:00001002"
           ],
           "out": [
               "urn:knowin-spec:property:xxx:00001002"
           ]
       }
   }
   ```
   
 #### 1.6 事件

 ##### 1.6.1 读取所有事件

 * 请求

   ```http
   GET /events
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "properties": [
               {
                   "namespace": "knowin-spec",
                   "name": "click",
                   "description": {
                       "zh-CN": "单击",
                       "zh-TW": "单击",
                       "en-US": "click"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.6.2 读取某个名字空间下的所有事件

 * 请求

   ```http
   GET /events?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "properties": [
               {
                   "namespace": "knowin-spec",
                   "name": "click",
                   "description": {
                       "zh-CN": "单击",
                       "zh-TW": "单击",
                       "en-US": "click"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.6.3 读取某个名字空间下的单个事件定义

 * 请求（指定名字空间和名称）

   ```http
   GET /event?ns=knowin-spec&name=click
   ```

 * 或者（指定type）

   ```http
   GET /event?type=urn:knowin-spec:event:click:0000aaaa
   ```
   
 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "type": "urn:knowin-spec:event:click:0000aaaa",
           "description": {
               "zh-CN": "单击",
               "zh-TW": "单击",
               "en-US": "click"
           },
           "arguments": [
               "urn:knowin-spec:property:a:00001001",
               "urn:knowin-spec:property:b:00001002"
           ]
       }
   }
   ```
   
 #### 1.7 格式

 ##### 1.7.1 读取所有格式

 * 请求

   ```http
   GET /formats
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "units": [
               {
                   "name": "uint8",
                   "ns": "knowin-spec",
                   "description": {
                       "zh-CN": "8位无符号整型",
                       "zh-TW": "8位无符号整型",
                       "en-US": "8bits unsigned integer"
                   }
               },
               {
                   "name": "string",
                   "ns": "homekit-spec",
                   "description": {
                       "zh-CN": "字符串",
                       "zh-TW": "字符串",
                       "en-US": "string"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.7.2 读取某个名字空间下的所有格式

 * 请求

   ```http
   GET /formats?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "units": [
               {
                   "name": "uint8",
                   "ns": "knowin-spec",
                   "description": {
                       "zh-CN": "8位无符号整型",
                       "zh-TW": "8位无符号整型",
                       "en-US": "8 bits unsigned integer"
                   }
               },
               {
                   "name": "string",
                   "ns": "knowin-spec",
                   "description": {
                       "zh-CN": "字符串",
                       "zh-TW": "字符串",
                       "en-US": "string"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.7.3 读取某个名字空间下的单个格式定义

 * 请求

   ```http
   GET /format?ns=knowin-spec&name=uint8
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "name": "uint8",
           "description": {
               "zh-CN": "8位无符号整型",
               "zh-TW": "8位无符号整型",
               "en-US": "8 bits unsigned integer"
           }
       }
   }
   ```
   
 #### 1.8 单位

 ##### 1.8.1 读取所有单位

 * 请求

   ```http
   GET /units
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "units": [
               {
                   "name": "percentage",
                   "ns": "knowin-spec",
                   "description": {
                       "zh-CN": "百分比",
                       "zh-TW": "百分比",
                       "en-US": "percentage"
                   }
               },
               {
                   "name": "seconds",
                   "ns": "homekit-spec",
                   "description": {
                       "zh-CN": "秒",
                       "zh-TW": "秒",
                       "en-US": "seconds"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.8.2 读取某个名字空间下的所有单位

 * 请求

   ```http
   GET /units?ns=knowin-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "units": [
               {
                   "name": "percentage",
                   "description": {
                       "zh-CN": "百分比",
                       "zh-TW": "百分比",
                       "en-US": "percentage"
                   }
               },
               {
                   "name": "seconds",
                   "description": {
                       "zh-CN": "秒",
                       "zh-TW": "秒",
                       "en-US": "seconds"
                   }
               }
           ]
       }
   }
   ```
   
 ##### 1.8.3 读取某个名字空间下的单个单位

 * 请求

   ```http
   GET /unit?ns=knowin-spec&name=percentage
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "name": "percentage",
           "description": {
               "zh-CN": "百分比",
               "zh-TW": "百分比",
               "en-US": "percentage"
           }
       }
   }
   ```

 

 ### 二、产品模板

 #### 2.1 读取所有模板

 * 请求

   ```http
   GET /templates
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "templates": [
               {
                   "type": "urn:xiot-spec:template:outlet:00000001",
                   "description": {
                       "zh-CN": "单键开关",
                       "zh-TW": "单键开关",
                       "en-US": "single switch"
                   }
               }
           ]
       }
   }
   ```

 #### 2.2 读取某个名字空间下的模板

 * 请求

   ```http
   GET /templates?ns=k-spec
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "templates": [
               {
                   "type": "urn:k-spec:template:outlet:00000001",
                   "description": {
                       "zh-CN": "单键开关",
                       "zh-TW": "单键开关",
                       "en-US": "single switch"
                   }
               }
           ]
       }
   }
   ```


 #### 2.3 读取单个模板定义

 * 请求

   ```http
   GET /template?type=urn:k-spec:template:switch:00000008:a:b:1
   ```

 * 应答

   ```json
   {
     "msg": "ok",
     "data": {
       "type": "urn:k-spec:template:switch:00000008:a:b:1",
       "description": "单键开关_TS",
       "services": [
         {
           "iid": 1,
           "type": "urn:k-spec:service:accessory-information:0000003e:a:b:1",
           "description": {
             "en_US": "Accessory Information",
             "zh_CN": "配件信息",
             "zh_TW": "配件信息"
           },
           "properties": [
             {
               "iid": 2,
               "type": "urn:k-spec:property:identify:00000014:a:b:1",
               "description": {
                 "en_US": "Identify",
                 "zh_CN": "認證",
                 "zh_TW": "Identify"
               },
               "format": "bool",
               "access": [
                 "write"
               ]
             },
             {
               "iid": 3,
               "type": "urn:k-spec:property:manufacturer:00000020:a:b:1",
               "description": {
                 "en_US": "Manufacturer",
                 "zh_CN": "生产商",
                 "zh_TW": "生產商"
               },
               "format": "string",
               "access": [
                 "read"
               ]
             },
             {
               "iid": 4,
               "type": "urn:k-spec:property:model:00000021:a:b:1",
               "description": {
                 "en_US": "Model",
                 "zh_CN": "模型",
                 "zh_TW": "模型"
               },
               "format": "string",
               "access": [
                 "read"
               ]
             }
           ]
         }
       ]
     }
   }
   ```

 ### 三、产品

 #### 3.1 读取所有产品信息

 * 请求

   ```http
   GET /products
   ```

 * 返回

   ```json
   {
       "msg": "ok",
       "data": {
           "products": [
               {
                   "id": 100,
                   "name": "如影小筒灯",
                   "description": {
                       "zh-CN": "灯",
                       "zh-TW": "灯",
                       "en-US": "lightbulb"
                   },
                   "template": "urn:xiot-spec:template:outlet:00000001",
                   "instances": [
                       {
                           "versions": 1,
                           "status": "debug",
                           "type": "urn:xiot-spec:device:outlet:00000001:a:b:1"
                       },
                       {
                           "versions": 2,
                           "status": "preview",
                           "type": "urn:xiot-spec:device:outlet:00000001:a:b:2"
                       },
                       {
                           "versions": 3,
                           "status": "released",
                           "type": "urn:xiot-spec:device:outlet:00000001:a:b:3"
                       }
                   ]
               }
           ]
       }
   }
   ```

 * status代表产品的状态

   | status      | 说明                           |
   | ----------- | ------------------------------ |
   | development | 开发中（极大可能会被修改）     |
   | preview     | 预览（上线中，有可能会被修改） |
   | released    | 正式版（已上线，不会再修改）   |

 #### 3.2 读取单个产品信息

 * 请求

   ```http
   GET /product?id=100
   ```

   或
   ```http
   GET /product?spec=xxx&model=xxx&vendor=xxx
   ```
 * 返回

   ```json
   {
       "msg": "ok",
       "data": {
           "id": 100,
           "name": "如影小筒灯",
           "description": {
               "zh-CN": "灯",
               "zh-TW": "灯",
               "en-US": "lightbulb"
           },
           "template": "urn:xiot-spec:template:outlet:00000001",
           "instances": [
               {
                   "versions": 1,
                   "status": "debug",
                   "type": "urn:xiot-spec:device:outlet:00000001:a:b:1"
               }
           ]
       }
   }
   ```

 #### 3.3 读取单个实例定义

 ##### 3.3.1 按照产品ID和版本读取

 * 请求

   ```http
   GET /instance?productId=16&version=1
   ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "type": "urn:homekit-spec:device:Switch:00000008:yingtuo:s2:1",
           "description": {
               "en_US": "DoubleSwitch",
               "zh_CN": "双路开关"
           },
           "services": [
               {
                   "iid": 1,
                   "type": "urn:homekit-spec:service:accessory-information:0000003e:yingtuo:s2:1",
                   "description": {
                       "zh_TW": "Accessory Information",
                       "en_US": "Accessory Information",
                       "zh_CN": "配件信息"
                   },
                   "properties": [
                       {
                           "iid": 2,
                           "type": "urn:homekit-spec:property:identify:00000014:yingtuo:s2:1",
                           "format": "bool",
                           "access": [
                               "write"
                           ]
                       },
                       {
                           "iid": 3,
                           "type": "urn:homekit-spec:property:manufacturer:00000020:yingtuo:s2:1",
                           "format": "string",
                           "access": [
                               "read"
                           ]
                       },
                       {
                           "iid": 4,
                           "type": "urn:homekit-spec:property:model:00000021:yingtuo:s2:1",
                           "format": "string",
                           "access": [
                               "read"
                           ]
                       },
                       {
                           "iid": 5,
                           "type": "urn:homekit-spec:property:name:00000023:yingtuo:s2:1",
                           "format": "string",
                           "access": [
                               "read"
                           ]
                       },
                       {
                           "iid": 6,
                           "type": "urn:homekit-spec:property:serial-number:00000030:yingtuo:s2:1",
                           "format": "string",
                           "access": [
                               "read"
                           ]
                       },
                       {
                           "iid": 7,
                           "type": "urn:homekit-spec:property:firmware-revision:00000052:yingtuo:s2:1",
                           "format": "string",
                           "access": [
                               "read"
                           ]
                       },
                       {
                           "iid": 8,
                           "type": "urn:homekit-spec:property:hardware-revision:00000053:yingtuo:s2:1",
                           "format": "string",
                           "access": [
                               "read"
                           ]
                       }
                   ]
               },
               {
                   "iid": 9,
                   "type": "urn:homekit-spec:service:switch:00000049:yingtuo:s2:1",
                   "description": {
                       "zh_TW": "Switch",
                       "en_US": "Switch",
                       "zh_CN": "左键"
                   },
                   "properties": [
                       {
                           "iid": 10,
                           "type": "urn:homekit-spec:property:on:00000025:yingtuo:s2:1",
                           "format": "bool",
                           "access": [
                               "read",
                               "write",
                               "notify"
                           ]
                       }
                   ]
               },
               {
                   "iid": 12,
                   "type": "urn:homekit-spec:service:switch:00000049:yingtuo:s2:1",
                   "description": {
                       "zh_TW": "Switch",
                       "en_US": "Switch",
                       "zh_CN": "右键"
                   },
                   "properties": [
                       {
                           "iid": 13,
                           "type": "urn:homekit-spec:property:on:00000025:yingtuo:s2:1",
                           "format": "bool",
                           "access": [
                               "read",
                               "write",
                               "notify"
                           ]
                       }
                   ]
               }
           ]
       }
   }
   ```

 ##### 3.3.2 按照deviceType读取

 * 请求
    ```http
    GET /instance?type=urn:homekit-spec:device:Switch:00000008:yingtuo:s2:1
    ```

 * 应答

   ```json
   同3.1
   ```
##### 3.3.3 根据DeviceType读取实例状态

 * 请求
    ```http
    GET /instance/status?type=urn:homekit-spec:device:Switch:00000008:yingtuo:s2:1
    ```

 * 应答

   ```json
   {
       "msg": "ok",
       "data": {
           "status": "development"
       }
   }
   ```
