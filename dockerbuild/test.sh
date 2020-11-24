#!/bin/bash

### below are manual executed!!!!
docker build -t tbs:v1 .
docker run -d -m 4G --name tbstest tbs:v1
docker exec -it tbstest /bin/sh


export image_id=
export version_number=v1
sudo docker tag $image_id registry.cn-shanghai.aliyuncs.com/freemanfxzhong/tianchi:$version_number
sudo docker push registry.cn-shanghai.aliyuncs.com/freemanfxzhong/tianchi:$version_number

