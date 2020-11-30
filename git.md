# 代码提交



## 一、 拉取代码

### 1. 从github拉取主仓库代码（建议使用ssh-key）

```bash
git clone xxxxxxx
```

### 2. 添加阿里云副仓库（建议使用ssh-key）

```bash
git remote add aliyun git@code.aliyun.com:xxxx/xxxx.git
```

### 3.  查看远程仓库，此时应该有2个仓库：origin和aliyun

```bash
git remote
```



## 二、提交代码

### 1. 提交
```bash
git commit .
```

###  2. 同步代码到2个仓库

```bash
./upload
```

