package com.ryxc.spider;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * Hello world!
 *
 */
public class CRDUApp 
{
    public static void main( String[] args )throws Exception{
    	//把hbase-site.xml放到classpath中
    	Configuration conf =  HBaseConfiguration.create();
        HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
        System.out.println(hBaseAdmin);
        
        //create 't2','f1','f2'
        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf("t2"));
        tableDesc.addFamily(new HColumnDescriptor("f1"));
        tableDesc.addFamily(new HColumnDescriptor("f2"));
        hBaseAdmin.createTable(tableDesc);
        
        hBaseAdmin.close();
    }
}
