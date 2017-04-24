package sample.change.me.demo_canvs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import java.util.Date;


public class MainActivity extends Activity {
    private static final String TAG="MainActivity";
    private Paint paint;
    private double second=0;
    private double minus=0;
    private double hour=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomView customView=new CustomView(this);
        setContentView(customView);
        //获取系统时间
        minus=Integer.parseInt(DateFormat.format("mm", getDate().getTime()).toString());
        hour=Integer.parseInt(DateFormat.format("hh", getDate().getTime()).toString())*5;
        second=Integer.parseInt(DateFormat.format("ss", getDate().getTime()).toString())-12;
    }

    private Date getDate(){
        return new Date(System.currentTimeMillis());
    }
    class CustomView extends View {
        public CustomView(Context context) {
            super(context);
            //new 一个画笔
            paint=new Paint();
            //设置画笔颜色
            paint.setColor(Color.YELLOW);
            //设置结合处的样子,Miter:结合处为锐角, Round:结合处为圆弧:BEVEL:结合处为直线。
            paint.setStrokeJoin(Paint.Join.ROUND);
            //设置画笔笔刷类型 如影响画笔但始末端
            paint.setStrokeCap(Paint.Cap.ROUND);
            //设置画笔宽度
            paint.setStrokeWidth(3);

        }
        @Override
        public void draw(Canvas canvas) {
            double startTime=System.currentTimeMillis();   //获取开始时间
            super.draw(canvas);
            //设置屏幕颜色，也可以利用来清屏。
            canvas.drawColor(Color.rgb(122,65,255));
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            canvas.translate(canvas.getWidth()/2, canvas.getHeight()/2); //将位置移动画纸的坐标点:150,150
            canvas.drawCircle(0, 0, 100, paint); //画圆圈

            //使用path绘制路径文字
            canvas.save();
            //向x左平移75，y上平移75
            canvas.translate(-75, -75);
            Path path = new Path();
            //画弧形，从-180的位置开始，到180度
            path.addArc(new RectF(0,0,150,150), -180, 180);
            Paint citePaint = new Paint(paint);
            //设置画笔文本字体大小
            citePaint.setTextSize(14);
            //设置画笔宽度
            citePaint.setStrokeWidth(1);
            //在左边偏离14
            //canvas.drawTextOnPath("http://www.wjb820728252a.com", path, 14, 0, citePaint);
            //回复画布位置
            canvas.restore();
            Paint tmpPaint = new Paint(paint); //小刻度画笔对象
            tmpPaint.setStrokeWidth(1);
            float  y=100;
            int count = 60; //总刻度数
            canvas.rotate(180+360/12,0f,0f); //旋转画纸
            for(int i=0 ; i <count ; i++){
                if(i%5 == 0){
                    canvas.drawLine(0f, y, 0, y+12f, paint);
                    canvas.drawText(String.valueOf(i/5+1), -4f, y+25f, tmpPaint);
                }else{
                    canvas.drawLine(0f, y, 0f, y +5f, tmpPaint);
                }
                canvas.rotate(360/count,0f,0f); //旋转画纸
            }
            canvas.save();
            canvas.save();//各个状态最初
            //绘制指针
            tmpPaint.setColor(Color.GRAY);
            //设置画笔宽度
            tmpPaint.setStrokeWidth(4);
            canvas.drawCircle(0, 0, 7, tmpPaint);
            tmpPaint.setStyle(Paint.Style.FILL);
            tmpPaint.setColor(Color.YELLOW);
            canvas.drawCircle(0, 0, 5, tmpPaint);
            paint.setColor(Color.RED);
            canvas.rotate(-30,0f,0f); //调整时针
            //绘制时针
            canvas.rotate((float) ((360/12/5*(hour+minus/12.0))%360),0f,0f);
            canvas.drawLine(0, -10, 0, 45, paint);

            canvas.restore();//回到初始状态
            canvas.rotate(25*360/12/5,0f,0f); //调整分针
            paint.setColor(Color.GREEN);
            //绘制分针
            canvas.rotate((float) ((360/12/5*minus)%360),0f,0f);
            canvas.drawLine(0, 10, 0, -65, paint);

            canvas.restore();//回到初始状态
            canvas.rotate(-30,0f,0f); //调整秒针
            //绘制秒针
            paint.setColor(Color.BLUE);
            canvas.rotate((float) ((360/12/5*second)%360),0f,0f); //旋转画纸,没秒旋转360/12/5度
            canvas.drawLine(0, -10, 0, 85, paint);
            paint.setColor(Color.WHITE);
            canvas.rotate(360/12/5);
            if(second==60){
                second=second%60;
                minus++;
                if(minus==60){
                    minus=minus%60;
                    hour+=5;
                    if(hour==60)
                        hour=hour%60;
                }
            }
            second++;
            double endTime=System.currentTimeMillis(); //获取结束时间
            //每隔1秒钟刷新页面
            postInvalidateDelayed((long) (1000-(endTime-startTime)));
        }
    }
}
