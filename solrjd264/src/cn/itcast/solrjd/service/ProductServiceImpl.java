package cn.itcast.solrjd.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.solrjd.dao.ProductDao;
import cn.itcast.solrjd.pojo.ResultModel;
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	private static final Integer PAGE_SIZE = 20;
	
	@Override
	public ResultModel findProductList(String queryString, String catalog_name, 
			String price, Integer page, String sort)
			throws Exception {
		
		//创建一个条件查询对象
		SolrQuery query = new SolrQuery();
		//设置参数
		//试着搜索默认域
		query.set("df", "product_keywords");
		//判断查询条件关键字
		if(queryString != null&&!"".equals(queryString)){
			query.setQuery(queryString);
		}else{
			query.setQuery("*:*");
		}
		
		//商品分类名称过滤
		if(catalog_name != null&&!"".equals(catalog_name)){
			query.setFilterQueries("product_catalog_name:"+catalog_name);
		}
		
		//判断过滤条件之价格
		if(price != null && !"".equals(price)){
			String[] split = price.split("-");
			query.setFilterQueries("product_price:[" + split[0] + " TO " + split[1] + "]");
		}
		
		//分页处理
		if(page == null){
			page = 1;
		}
		//初始化分页
		Integer start = (page - 1) * PAGE_SIZE;
		//设置分页
		query.setStart(start);
		query.setRows(PAGE_SIZE);		
		

		//排序条件
		if(!"1".equals(sort)){
			query.setSort("product_price",ORDER.asc);
		}else{
			query.setSort("product_price",ORDER.desc);
		}
		
		//设置高亮
		query.setHighlight(true);
		//设置高亮的域
		query.addHighlightField("product_name");
		//设置高亮关键字的前缀
		query.setHighlightSimplePre("<span style=\"color:red\">");
		//设置高亮关键字的后缀
		query.setHighlightSimplePost("</span>");
		
		//执行查询，调用dao返回ResultModel
		ResultModel resultModel = productDao.findProductList(query);
		//封装model数据
		//设置当前页
		resultModel.setCurPage(page);
		//设置总页数
		Long pageCount = resultModel.getRecordCount() / PAGE_SIZE;
		if(resultModel.getRecordCount() % PAGE_SIZE > 0){
			pageCount++;
		}
		resultModel.setPageCount(pageCount);
		
		return resultModel;
	}

}
