# Component
组件化框架

# Android组件化
## 1.什么是组件化

百度百科：组件化是一种高效的处理复杂应用系统，更好的明确功能模块作用的方式。

Android：组件化就是把一个功能完整的 App 拆分成多个业务模块,每个业务模块可以独立编译和运行, 也可以任意组合成另一个新的 App 或模块, 每个模块即不相互依赖但又可以相互交互, 遇到某些特殊情况甚至可以升级或者降级

## 2.为什么要组件化

现有项目随着业务迭代、需求的增加，规模变得越来越大随之带来各种问题：

2.1. 业务之前, 错中复杂相互交织。问题：小更改可能引起新问题，牵一发而动全身，要熟悉大量代码才敢动手

2.2. 模块之间, 代码边界的模糊。 问题：代码冲突时有发生

2.3. 每次更改都需要全量编译。问题：编译时间不断增加，开发效率下降

## 3.分析现有的组件化方案

结构：多工程 + 多模块

实践：使用git submodule创建多个子仓库管理模块代码，将模块代码打包上传到私有maven仓库，通过远程依赖代码隔离

## 4.如何做组件化

kucoin采用 单工程 + 多模块 结构，模块之间通过源码方式依赖并没有上传到maven仓库管理组件版本，比较适用于
当前开发人员较小，产品结构单一团队，有助于开发效率的提高。

### 4.1. 架构图：

宿主层：APP

业务层：认证模块、资产模块、个人模块、交易模块

基础层：CommonSDK、CommonRegister、RouterSDK、RouterRegister

### 4.2. 架构图介绍

架构共3层，从低到高依次是基础层、业务层、宿主层

宿主层：作为 APP 壳，将需要的模块组装成一个完整 APP

业务层：根据业务需求拆分后的业务模块、各个业务模块之前互不依赖，但又可以相互交互(确定业务边界)

基础层：主要为业务层提供基础功能服务

### 4.2.1 CommonSDK、CommonRegister

通过生命周期委托方式，各个模块管理自己的生命周期相互解耦，方便在library和application之间切换

### 4.2.2 RouterSDK、RouterRegister

主要为业务层提供跨组件跳转支持，通过声明式协议方式，实现"业务组件"跳转 和 暴露服务 
如：交易模块需要获取用户登录状态，就需要通过认证模块的提供的服务来获取

## 5.项目讲解

### 5.1 如何让组件独立运行

在项目跟目录的gradle.properies中，改变isBuildModule的值即可

isBuildModule=true // true: 可以使每个组件独立运行，false: 所有组件集成到宿主app中

### 5.2 配置AndroidManifest

组件独立运行和集成到宿主需要配置不同的manifest，在独立运行时需要其中一个activity配置入口，而集成到宿主时，
则依赖于宿主入口

apply from: "$gradle_root/component_config.gradle"
