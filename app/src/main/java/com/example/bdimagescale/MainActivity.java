package com.example.bdimagescale;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final int FIT_INSIDE = 1;
	private static final int CROP = 2;
	
	private ImageView iv1;// Ĭ����������,��FIT_INSIDE
	private ImageView iv2;// centerCrop��������,��CROP
	private ImageView iv3;// Ĭ����������,��FIT_INSIDE
	private ImageView iv4;// centerCrop��������,��CROP
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		// 10000����������
//		for(int i=0; i<20; i++) {
//			int width = (int)(Math.random()*9000+1000);
//			int height = (int)(Math.random()*9000+1000);
//			test(width, height, 400, 200, 200, 200);
////		compareInSampleSize(width, height, 400, 200);
//		}
		
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		testLongImage();
//		testNormalImage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// ���ݶԱ�
	public void test(int width, int height, 
			int limitWidth, int limitHeight, 
			int viewWidth, int viewHeight) {
		System.out.println("ԭͼ���="+width+":"+height);
		System.out.println("�޶����="+limitWidth+":"+limitHeight);
		System.out.println("�ؼ���С="+viewWidth+":"+viewHeight);
		float densityOfINSIDE0 = calculateImagePxDensityOfINSIDE(limitWidth, limitHeight, viewWidth, viewHeight);
		System.out.println("Ĭ�ϼ�INDESE�����Ŀ�������ܶ�Ϊ="+densityOfINSIDE0);
		float densityOfCROP0 = calculateImagePxDensityOfCROP(limitWidth, limitHeight, viewWidth, viewHeight);
		System.out.println("CROP�����Ŀ�������ܶ�Ϊ="+densityOfCROP0);
		System.out.println("---------------------");
		
		
		int inSampleSize1 = calculateInSampleSize(width, height, limitWidth, limitHeight);
		int imageWidth = width/inSampleSize1;
		int imageHeight = height/inSampleSize1;
		System.out.println("�ٷ�:��Ӧ���ź�ͼƬ��С="+imageWidth+":"+imageHeight);
		float densityOfINSIDE = calculateImagePxDensityOfINSIDE(imageWidth, imageHeight, viewWidth, viewHeight);
		System.out.println("Ĭ�ϼ�INDESE����������ܶ�Ϊ="+densityOfINSIDE);
		float densityOfCROP = calculateImagePxDensityOfCROP(imageWidth, imageHeight, viewWidth, viewHeight);
		System.out.println("CROP����������ܶ�Ϊ="+densityOfCROP);
		System.out.println("---------------------");
		
		
		int reWidth = getResizedDimension(limitWidth, limitHeight, width, height);
		int reHeight = getResizedDimension(limitHeight, limitWidth, height, width);
//		System.out.println("������޶����="+reWidth+":"+reHeight);
		int inSampleSize2 = findBestSampleSize(width, height, reWidth, reHeight);
		int imageWidth2 = width/inSampleSize2;
		int imageHeight2 = height/inSampleSize2;
		System.out.println("Volley:��Ӧ���ź�ͼƬ��С="+imageWidth2+":"+imageHeight2);
		float densityOfINSIDE2 = calculateImagePxDensityOfINSIDE(imageWidth2, imageHeight2, viewWidth, viewHeight);
		System.out.println("Ĭ�ϼ�INDESE����������ܶ�Ϊ="+densityOfINSIDE2);
		float densityOfCROP2 = calculateImagePxDensityOfCROP(imageWidth2, imageHeight2, viewWidth, viewHeight);
		System.out.println("CROP����������ܶ�Ϊ="+densityOfCROP2);
		System.out.println("---------------------");
		
		
		int inSampleSize3OfINSIDE = computeImageSampleSize(width, height, limitWidth, limitHeight, FIT_INSIDE, true);
		int imageWidthOfINSIDE = width/inSampleSize3OfINSIDE;
		int imageHeightOfINSIDE = height/inSampleSize3OfINSIDE;
		System.out.println("UIL FIT_INSIDE:��Ӧ���ź�ͼƬ��С="+imageWidthOfINSIDE+":"+imageHeightOfINSIDE);
		float densityOfINSIDE3 = calculateImagePxDensityOfINSIDE(imageWidthOfINSIDE, imageHeightOfINSIDE, viewWidth, viewHeight);
		System.out.println("Ĭ�ϼ�INDESE����������ܶ�Ϊ="+densityOfINSIDE3);
		int inSampleSize3OfCROP = computeImageSampleSize(width, height, limitWidth, limitHeight, CROP, true);
		int imageWidthOfCROP = width/inSampleSize3OfCROP;
		int imageHeightOfCROP = height/inSampleSize3OfCROP;
		System.out.println("UIL CROP:��Ӧ���ź�ͼƬ��С="+imageWidthOfCROP+":"+imageHeightOfCROP);
		float densityOfCROP3 = calculateImagePxDensityOfCROP(imageWidthOfCROP, imageHeightOfCROP, viewWidth, viewHeight);
		System.out.println("CROP����������ܶ�Ϊ="+densityOfCROP3);
		System.out.println("---------------------");
		
		System.out.println();
	}
	
	// inSampleSize�Ա�
	public void compareInSampleSize(
			int imageWidth, int imageHeight, int reqWidth, int reqHeight) {
		System.out.println("ԭͼƬ " + imageWidth+":"+imageHeight);
		System.out.print("�ٷ� " + calculateInSampleSize(imageWidth, imageHeight, reqWidth, reqHeight));
		System.out.print(" ... ");
		System.out.print("UIL " + computeImageSampleSize(imageWidth, imageHeight, reqWidth, reqHeight, CROP, true));
		System.out.print(" ... ");
		System.out.print("Volley " + findBestSampleSize(imageWidth, imageHeight, reqWidth, reqHeight));
		System.out.println();
		System.out.println();
	}
	
	public void testLongImage() {
		Bitmap vbitmap = decodeByVolley(getResources(), R.drawable.longimage, 300, 300);
		// UIL����л������жϴ���viewScaleType,����Ϊ�˼���ʱ��д��
//		Bitmap ubitmap_inside = decodeByUIL(getResources(), R.drawable.longimage, 300, 300, FIT_INSIDE);
//		Bitmap ubitmap_crop = decodeByUIL(getResources(), R.drawable.longimage, 300, 300, CROP);
//
		iv1.setImageBitmap(vbitmap);
		iv2.setImageBitmap(vbitmap);
//
//		iv3.setImageBitmap(ubitmap_inside);
//		iv4.setImageBitmap(ubitmap_crop);
	}
	
	public void testNormalImage() {
		Bitmap vbitmap = decodeByVolley(getResources(), R.drawable.normalimage, 300, 300);
		// UIL����л������жϴ���viewScaleType,����Ϊ�˼���ʱ��д��
		Bitmap ubitmap_inside = decodeByUIL(getResources(), R.drawable.normalimage, 300, 300, FIT_INSIDE);
		Bitmap ubitmap_crop = decodeByUIL(getResources(), R.drawable.normalimage, 300, 300, CROP);
		
		iv1.setImageBitmap(vbitmap);
		iv2.setImageBitmap(vbitmap);
		
		iv3.setImageBitmap(ubitmap_inside);
		iv4.setImageBitmap(ubitmap_crop);
	}
	
	public Bitmap decodeByVolley(Resources res, int resId,
	        int limitWidth, int limitHeight) {
	    
	    // ���Ȳ�����ͼƬ,����ȡͼƬ�ߴ�
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    // ��inJustDecodeBounds��Ϊtrueʱ,�������ͼƬ����ȡͼƬ�ߴ���Ϣ
	    options.inJustDecodeBounds = true;
	    // ��ʱ���ὫͼƬ��Ϣ�ᱣ����options������,decode�������᷵��bitmap����
	    BitmapFactory.decodeResource(res, resId, options);

	    int reWidth = getResizedDimension(limitWidth, limitHeight, options.outWidth, options.outHeight);
		int reHeight = getResizedDimension(limitHeight, limitWidth, options.outHeight, options.outWidth);

        Log.e("TAG","width="+reWidth+",height="+reHeight);

	    // ����ѹ������,��inSampleSize=4ʱ,ͼƬ��ѹ����ԭͼ��1/4
	    options.inSampleSize = findBestSampleSize(options.outWidth, options.outHeight, reWidth, reHeight);

	    // ��inJustDecodeBounds��Ϊfalseʱ,BitmapFactory.decode...�ͻ᷵��ͼƬ������
	    options.inJustDecodeBounds = false;
	    // ���ü���ı���ֵ��ȡѹ�����ͼƬ����
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public Bitmap decodeByUIL(Resources res, int resId,
			int limitWidth, int limitHeight, int scaleType) {
		
		// ���Ȳ�����ͼƬ,����ȡͼƬ�ߴ�
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// ��inJustDecodeBounds��Ϊtrueʱ,�������ͼƬ����ȡͼƬ�ߴ���Ϣ
		options.inJustDecodeBounds = true;
		// ��ʱ���ὫͼƬ��Ϣ�ᱣ����options������,decode�������᷵��bitmap����
		BitmapFactory.decodeResource(res, resId, options);
		
		// ����ѹ������,��inSampleSize=4ʱ,ͼƬ��ѹ����ԭͼ��1/4
		options.inSampleSize = computeImageSampleSize(
				options.outWidth, options.outWidth, limitWidth, limitHeight, scaleType, true);
		
		// ��inJustDecodeBounds��Ϊfalseʱ,BitmapFactory.decode...�ͻ᷵��ͼƬ������
		options.inJustDecodeBounds = false;
		// ���ü���ı���ֵ��ȡѹ�����ͼƬ����
		return BitmapFactory.decodeResource(res, resId, options);
	}

	
	// ����FIT_INSIDEʱ�����ܶ�
	static float calculateImagePxDensityOfINSIDE(
			int imageWidth, int imageHeight, int viewWidth, int viewHeight) {
		float density = -1;
		if(imageWidth >= imageHeight) {
			density = (float)imageWidth/(float)viewWidth;
		} else {
			density = (float)imageHeight/(float)viewHeight;
		}
		return density*density;
	}
	
	// ����CROPʱ�����ܶ�
	static float calculateImagePxDensityOfCROP(
			int imageWidth, int imageHeight, int viewWidth, int viewHeight) {
		float density = -1;
		if(imageWidth >= imageHeight) {
			density = (float)imageHeight/(float)viewHeight;
		} else {
			density = (float)imageWidth/(float)viewWidth;
		}
		return density*density;
	}

	////////////////////	�ٷ�		////////////////////////////////
    int calculateInSampleSize(int width, int height,
                 int reqWidth, int reqHeight) {
          // ��ʼ��ѹ������Ϊ1
          int inSampleSize = 1;

          // ��ͼƬ���ֵ�κ�һ����������ѹ��ͼƬ���ֵʱ,����ѭ������ϵͳ
          if (height > reqHeight || width > reqWidth) {

                 final int halfHeight = height / 2;
                 final int halfWidth = width / 2;

                 // ѹ������ֵÿ��ѭ����������,
                 // ֱ��ԭͼ���ֵ��һ�����ѹ��ֵ��~����������ֵΪֹ(��>�ĳ���>=)
                 while ((halfHeight / inSampleSize) >= reqHeight
                            && (halfWidth / inSampleSize) >= reqWidth) {
                      inSampleSize *= 2;
                }
          }
          return inSampleSize;
    }
    
    ////////////////////	UIL		////////////////////////////////
    int computeImageSampleSize(
    		int width, int height, 
			int limitWidth, int limitHeight, 
			int viewScaleType,
			boolean powerOf2Scale) {

		int scale = 1;

		int widthScale = width / limitWidth;
		int heightScale = height / limitHeight;

		switch (viewScaleType) {
			case FIT_INSIDE:
				if (powerOf2Scale) {
					while (width / 2 >= limitWidth || height / 2 >= limitHeight) { // ||
						width /= 2;
						height /= 2;
						scale *= 2;
					}
				} else {
					scale = Math.max(widthScale, heightScale); // max
				}
				break;
			case CROP:
				if (powerOf2Scale) {
					while (width / 2 >= limitWidth && height / 2 >= limitHeight) { // &&
						width /= 2;
						height /= 2;
						scale *= 2;
					}
				} else {
					scale = Math.min(widthScale, heightScale); // min
				}
				break;
		}

		if (scale < 1) {
			scale = 1;
		}

		return scale;
	}
    
    ////////////////////	Volley		////////////////////////////////
    int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }
    
    int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
            int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }
    
}


