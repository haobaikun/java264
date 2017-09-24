package cn.itcast.solrjd.service;

import cn.itcast.solrjd.pojo.ResultModel;

public interface ProductService {

	//参数为页面传递的普通对象
	public ResultModel findProductList(String queryString,String catalog_name,String price,
			Integer page,String sort)throws Exception;
}
