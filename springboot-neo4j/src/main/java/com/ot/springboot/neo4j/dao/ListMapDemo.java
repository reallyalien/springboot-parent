package com.ot.springboot.neo4j.dao;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ListMapDemo {

    public static void main(String[] args) {
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("name", "入库测试1");
        map1.put("dataTotal", "10");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("name", "读取测试1");
        map2.put("dataTotal", "10");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("name", "入库测试1");
        map3.put("readData", "6");
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("name", "读取测试1");
        map4.put("readData", "5");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        //=======================================================
        //记录已经处理过的id
       Set<Integer> integerSet=new HashSet<>();
        List<Map<String,String>> result=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //如果在while循环当中处理过，直接跳过
            if (integerSet.contains(i)) continue;
            //记录临时变量i0
            int i0=i;
            Map<String, String> map = list.get(i);
            String name = map.get("name");
            String dataTotal = map.get("dataTotal");
            String readData = map.get("readData");
            //判断dataTotal还是readTotal
            boolean flag = true;
            int local = 0;
            if (StringUtils.isNotBlank(dataTotal)) {
                local = Integer.parseInt(dataTotal);
            } else {
                flag = false;
                local = Integer.parseInt(readData);

            }
            //返回的新的变量
            String newStr = "";
            while (++i < list.size()) {
                //如果map当中的name的value等于while循环外面的value
                boolean b = list.get(i).get("name").equals(name);
                if (b){
                    //把这个i即位置添加到集合当中
                    integerSet.add(i);
                    String dataTotal1 = list.get(i).get("dataTotal");
                    String readData1 = list.get(i).get("readData");
                    int local1 = 0;
                    if (StringUtils.isNotBlank(dataTotal1)) {
                        local1 = Integer.parseInt(dataTotal1);
                    } else {
                        local1 = Integer.parseInt(readData1);
                    }
                    if (flag) {
                        newStr = String.valueOf(local - local1);
                    }
                    //既然已经找到，直接break，
                    break;
                }else {
                    continue;
                }
            }
            HashMap<String, String> localMap = new HashMap<>();
            localMap.put("name",name);
            localMap.put("newStr",newStr);
            result.add(localMap);
            //将临时变量i0重新赋值给i，因为i在while循环当中已经改变了，
            i=i0;
        }
        System.out.println(result);
    }
}
