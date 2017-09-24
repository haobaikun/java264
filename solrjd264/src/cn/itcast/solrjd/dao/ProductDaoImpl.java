package cn.itcast.solrjd.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.itcast.solrjd.pojo.ProductModel;
import cn.itcast.solrjd.pojo.ResultModel;
@Repository
public class ProductDaoImpl implements ProductDao {
/**
 * 之前用mybatis框架的时候，还得需要扫描mapper包，要把接口的类扫描到内存里面去
 * 然后再注入
 * 
 * 而我们在这里不需要扫描，我们要在service调用的dao的话就需要加一个注解
 */
	
	@Autowired
	private HttpSolrServer solrServer;
	
	@Override
	public ResultModel findProductList(SolrQuery query) throws Exception {
		//查询
		QueryResponse queryResponse = solrServer.query(query);
		//返回结果集
		SolrDocumentList documentList = queryResponse.getResults();
		//总记录数
		Long productCount = documentList.getNumFound();
		//创建商品集合
		List<ProductModel> modelList = new ArrayList<>();
		
		for (SolrDocument doc : documentList) {
			String productName = "";
			//获取高亮
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(doc.get("id")).get("product_name");
			if(list != null){
				productName = list.get(0);
			}else{
				productName = String.valueOf(doc.get("product_name"));
			}
			//放在对象中
			ProductModel model = new ProductModel();
			model.setPid(String.valueOf(doc.get("id")));
			model.setName(productName);
			model.setCatalog_name(String.valueOf(doc.get("product_catalog_name")));
			model.setPicture(String.valueOf(doc.get("product_picture")));
			if(String.valueOf(doc.get("product_price")) != null
					&& !"".equals(String.valueOf(doc.get("product_price")))){
				model.setPrice(Float.parseFloat(String.valueOf(doc.get("product_price"))));
			}
			//把对象放到集合中
			modelList.add(model);
		}
		//封装返回结果集对象
		ResultModel resultModel = new ResultModel();
		//设置参数
		resultModel.setProductList(modelList);
		//设置总记录数
		resultModel.setRecordCount(productCount);
		
		return resultModel;
	}

}
