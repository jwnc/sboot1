package com.wnc.sboot1.small.meituan;

import translate.util.JsoupHelper;

public class MT {
	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= 10; i++) {
			String documentResult = JsoupHelper.getJsonResult(
					"http://cq.meituan.com/meishi/api/poi/getPoiList?cityName=重庆&cateId=17&areaId=0&sort=&dinnerCountAttrId=&page="
							+ i
							+ "&userId=&uuid=569ac4a313f346729358.1529218154.1.0.0&platform=1&partner=126&originUrl=http%3A%2F%2Fcq.meituan.com%2Fmeishi%2Fc17%2Fpn2%2F&riskLevel=1&optimusCode=1&_token=eJyFT01vozAQ%2FS%2B%2BFsXYgA2RegDSXTChTYA2kKoHQhICNA4BA1VW%2B9%2FXlbKHPe3pfczTm5lfoPP3YI5U1VJVBYyHDswBmqkzAyhA9HJiYAtjhFRdQ0QBxb%2Bebkpv170twPydYl0hGv34NiKp35FGiEKJ%2BaHcKZZUhrD%2BnfFlBJyEaOcQFtfZ%2BVCJIeez4nKGkvenChaIwpZjKC%2F5bw7IynMiKyU2d8zvKP7qUP4my%2Fqq5JId2LSvd0LYt6e1k4poYrZpcp3Y0xRG6yzcxsTWA9ycMvO4NAoX%2BXYxxJ52UZ392VuojFcZThrXors84bWnPi1WU7U61hWxjymFK4%2BOWf%2BalWvSRtz4bHkWB8nLz01ptE4ufvj1Vw2DxXPa1rcKLR%2FiaWt3lRFwnV1Tl1xGZrzVTc5oaJRJNFask3tcNtbNA1t3YfTZW9oQLjeDG1UBvjoQX8Xw3H5t2umYBpC%2B3qywR5wUMUS5k9eWdilalu%2Bpt4UvO%2Bb35eMj%2BP0HguqaUw%3D%3D");
			System.out.println(documentResult);
		}
	}
}
