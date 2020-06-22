 # 不加此选项，maven编译每次都会变成1.5版本
 <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
  


# git命令
git init
git add .
git commit -m 'init'

git remote add origin http://github.com/reallyalien/
git push -u origin master

git branch dev-login
git checkout dev-login
git branch -d dev-login
git merge dev-login //在master分支
git checkout -b dev-login //切换并创建分支

