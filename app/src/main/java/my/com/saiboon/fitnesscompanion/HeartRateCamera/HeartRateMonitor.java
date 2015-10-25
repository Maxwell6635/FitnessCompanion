package my.com.saiboon.fitnesscompanion.HeartRateCamera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang3.ArrayUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import my.com.saiboon.fitnesscompanion.R;

public class HeartRateMonitor extends Activity {

    public static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    // private static View image = null;
    private static TextView text = null;

    private static PowerManager.WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 100;
    private static final int[] averageArray = new int[averageArraySize];

    public static enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 14;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static final long[] timesArray = new long[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    private static GraphView graphView;
   // private static GraphViewSeries exampleSeries;
    private static LineGraphSeries<DataPoint> mSeries1;

    static int counter = 0;

    private static final int sampleSize = 256;
    private static final CircularFifoQueue sampleQueue = new CircularFifoQueue(
            sampleSize);
    private static final CircularFifoQueue timeQueue = new CircularFifoQueue(
            sampleSize);

    public static final CircularFifoQueue bpmQueue = new CircularFifoQueue(40);

    private static final FFT fft = new FFT(sampleSize);

    private Metronome metronome;

//	static BufferedWriter out;

    private boolean streamData = false;
    public static DatagramSocket mSocket = null;
    public static DatagramPacket mPacket = null;
    TextView mIP_Adress;
    TextView mPort;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_monitor);
        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        graphView = (GraphView) findViewById(R.id.graph1);

        // image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm
                .newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");




       /*this.graphView = new LineGraphView(this // context
                , "Heart rate" // heading
        );*/
        //graphView.setScrollable(true);

        /*this.exampleSeries = new GraphViewSeries("Heart rate",
                new GraphViewSeries.GraphViewSeriesStyle(Color.RED, 8),
                new GraphView.GraphViewData[] {});
        this.graphView.addSeries(this.exampleSeries);*/

        mSeries1 = new LineGraphSeries<DataPoint>();
        mSeries1.setTitle("Heart Rate");
        mSeries1.setColor(Color.RED);

        graphView.addSeries(mSeries1);




        // this.graphView.addSeries(new GraphViewSeries(new
        // GraphView.GraphViewData[] {
        // new GraphView.GraphViewData(1, 2.0d)
        // , new GraphView.GraphViewData(2, 1.5d)
        // , new GraphView.GraphViewData(3, 2.5d)
        // , new GraphView.GraphViewData(4, 1.0d)
        // })); // data


        /*graphView.setViewPort(0, 60);
        graphView.setVerticalLabels(new String[]{""});
        graphView.setHorizontalLabels(new String[]{""});
        graphView.getGraphViewStyle().setVerticalLabelsWidth(1);*/

        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getGridLabelRenderer().setLabelVerticalWidth(1);



        //Set View Port
       /* graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(60);

        // set manual Y bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(60);*/

        graphView.setBackgroundColor(Color.WHITE);



    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();

        camera = Camera.open();
        startTime = System.currentTimeMillis();

        // File file = new File(this.getFilesDir(), "data.txt");
        // try {
        // out = new BufferedWriter(new FileWriter(file));
        // } catch (IOException e) {
        // Log.e(TAG,"unexpected Error", e);
        // e.printStackTrace();
        // }

        metronome = new Metronome(this);
        metronome.start();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
        // try {
        // out.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        bpm = -1;
    }

    public static int bpm;
    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null)
                throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null)
                throw new NullPointerException();

            if (!processing.compareAndSet(false, true))
                return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(),
                    height, width);

            // try {
            // out.write(imgAvg + ",");
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

            sampleQueue.add((double) imgAvg);
            timeQueue.add(System.currentTimeMillis());

            double[] y = new double[sampleSize];
            double[] x = ArrayUtils.toPrimitive((Double[]) sampleQueue
                    .toArray(new Double[0]));
            long[] time = ArrayUtils.toPrimitive((Long[]) timeQueue
                    .toArray(new Long[0]));

            if (timeQueue.size() < sampleSize) {
                processing.set(false);

                return;
            }

            double Fs = ((double) timeQueue.size())
                    / (double) (time[timeQueue.size() - 1] - time[0]) * 1000;

            fft.fft(x, y);

            int low = Math.round((float) (sampleSize * 40 / 60 / Fs));
            int high = Math.round((float) (sampleSize * 160 / 60 / Fs));

            int bestI = 0;
            double bestV = 0;
            for (int i = low; i < high; i++) {
                double value = Math.sqrt(x[i] * x[i] + y[i] * y[i]);

                if (value > bestV) {
                    bestV = value;
                    bestI = i;
                }
            }

            bpm = Math.round((float) (bestI * Fs * 60 / sampleSize));
            bpmQueue.add(bpm);

            text.setText(String.valueOf(bpm));
            new UDPThread()
                    .execute(bpm + ", " + System.currentTimeMillis());

            counter++;
            /*exampleSeries.appendData(new GraphView.GraphViewData(counter,
                    imgAvg), true, 1000);*/

            mSeries1.appendData(new DataPoint(counter,
                   imgAvg), true, 1000);
            processing.set(false);

        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height="
                        + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height,
                                                      Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea)
                        result = size;
                }
            }
        }

        return result;
    }


    private void stop_UDP_Stream() {
        if (mSocket != null)
            mSocket.close();
        mSocket = null;
        mPacket = null;

    }

    private boolean isOnWifi() {
        ConnectivityManager conman = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return conman.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
    }
}
