package cn.itcast.solrjd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.solrjd.pojo.ResultModel;
import cn.itcast.solrjd.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping("list")
	public String list(String queryString,String catalog_name,String price,
			Integer page,String sort,Model model) throws Exception{
		ResultModel resultModel = productService.findProductList(queryString, catalog_name, price, page, sort);
		
		//条件回显
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);
		
		model.addAttribute("result", resultModel);
		return "product_list";
	}
}
