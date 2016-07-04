package com.ryxc.spider;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TestHDFS {
	private  FileSystem fileSystem ;
	public static String HDFS_PATH = "hdfs://fs162.nat123.net:8020/";
	
	@Before
	public void before() throws IOException, URISyntaxException{
	     fileSystem =  FileSystem.get(new URI(HDFS_PATH), new Configuration());
	}
	
	/**
	 * @throws IOException
	 */
	@Test
	public void getHDFSNodes() throws IOException{
        DistributedFileSystem hdfs = (DistributedFileSystem)fileSystem;
        DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();
        for(int i=0;i<dataNodeStats.length;i++){
        	System.out.println("DataNode_"+i+"_Name:"+dataNodeStats[i].getHostName());
        }
        
	}
	
	/**
	 * @throws IOException
	 */
	@Test
	public void getFileLocal() throws IOException{
		FileStatus fileStatus = fileSystem.getFileStatus(new Path("/testJava5"));
		BlockLocation[] blockLocation = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
		for(int i=0;i<blockLocation.length;i++){
			String [] hosts = blockLocation[i].getHosts();
			for(int j=0;j<hosts.length;j++){
				System.out.println("block_"+i+"_location:"+hosts[j]);
			}
			String [] names = blockLocation[i].getNames();
			for(int t=0;t<hosts.length;t++){
				System.out.println("block_"+i+"_location:"+names[t]);
			}
			
			System.out.println(blockLocation[i].getTopologyPaths()[1]);
			
		}
        
	}
	
	@Test
	public void createFile() throws IOException{
		byte[] buff="hello hadoop world! \n".getBytes();
		Path dfs=new Path("/testJava5");
		FSDataOutputStream outputStream=fileSystem.create(dfs,(short)5);
		outputStream.write(buff,0,buff.length);
	}
	
}
