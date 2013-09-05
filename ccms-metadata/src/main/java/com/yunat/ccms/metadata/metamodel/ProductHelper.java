package com.yunat.ccms.metadata.metamodel;

import com.yunat.ccms.metadata.face.FaceProduct;
import com.yunat.ccms.metadata.face.FaceProductKeyword;

public class ProductHelper {

	public static final char SPLIT = 1; // "\\x01";

	// "F:45L:61559109P:10347207133P:10347916849"
	public static FaceProduct parseUIData(String products) {

		if (products == null || "".equals(products)) {

			return null;
		}

		FaceProduct face = new FaceProduct();

		String[] productsArr = products.split(SPLIT + "");
		for (String product : productsArr) {

			if ("".equals(product) || product == null) {

				continue;
			}

			if (product.startsWith("P:")) {

				String id = product.replaceFirst("P:", "");
				face.getIds().add(id);
			}

			if (product.startsWith("F:")) {

				FaceProductKeyword fkw = new FaceProductKeyword();
				String[] kwArr = product.replaceFirst("F:", "").split(":");
				String[] productNames = kwArr[0].split(" ");

				for (int i = 0; i < productNames.length; i++) {
					fkw.getKeywords().add(productNames[i].trim());
				}

				if (kwArr.length == 2) {// 若指定了店铺
					fkw.setDpId(kwArr[1]);
				}
				face.getKeys().add(fkw);
			}
		}

		return face;
	}
}
