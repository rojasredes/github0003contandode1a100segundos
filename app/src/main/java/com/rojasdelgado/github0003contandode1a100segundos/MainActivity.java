package com.rojasdelgado.github0003contandode1a100segundos;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    final int contarhasta=100; //cuento hasta 5, porque contar hasta 100 me hace esperar 100 segundos sin ver nada
    TextView etiqueta;
    int contador;
    CountDownLatch semaforo= new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etiqueta=(TextView)findViewById(R.id.etiqueta);
        etiqueta.setText("Vamos a contar de 1 a 100");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        etiqueta.setText("Empezamos...");
        Log.d("onStart()","Empezamos....");

        //contarenelmismohilo(); //esto no se ve
        //contarenotrohilo(); //esto causa un error android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        //contarenotrohilomodificandodesdeelmismohilo(); //esto tampoco se ve, y además se ejecuta fuera de secuencia
        //contarenotrohilomodificandodesdeelmismohiloyeperandoaquetermine(); //esto no se ve y además no para
        //contarenotrohilohandler(); //esto no se ve, y se ejecuta fuera de secuencia
        //contarenotrohilohandlerpost(); //esto no se ve
        contardeunoenuno(); //esto se ve, pero se ejecuta fuera de secuencia
        //VOY POR AQUI ESTO NO SÉ POR QUÉ HA DEJADO DE VERSE Y DE EJECUTARSE
        //contardeunoenunoyesperar();



        etiqueta.setText("Terminamos.");
        Log.d("onStart()","Terminamos.");
    }

    protected void contar()
    {
        for(int i=1;i<=contarhasta;i++)
        {
            etiqueta.setText(Integer.valueOf(i).toString());
            Log.d("contar()",Integer.valueOf(i).toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void contarwait()
    {
        for(int i=1;i<=contarhasta;i++)
        {
            etiqueta.setText(Integer.valueOf(i).toString());
            Log.d("contar()",Integer.valueOf(i).toString());
            try {
                wait(1000); //esto causa un error java.lang.IllegalMonitorStateException: object not locked by thread before wait()
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean contaruno()
    {
        boolean cancelarcontar=false;
        contador++;
        etiqueta.setText(Integer.valueOf(contador).toString());
        Log.d("contar()",Integer.valueOf(contador).toString());
        if(contador>=contarhasta) {
            cancelarcontar=true;
        }
        return cancelarcontar;
    }

    protected void contarenelmismohilo()
    {
        contar();
    }

    protected void contarenotrohilo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contar();
            }
        }).start();
    }

    protected void contarenotrohilomodificandodesdeelmismohilo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contar();
                    }
                });
            }
        }).start();
    }

    protected void contarenotrohilomodificandodesdeelmismohiloyeperandoaquetermine()
    {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contar();
                        cancel();
                    }
                });
            }
        }, 0, 1000);
    }

    protected void contarenotrohilohandler()
    {
        Handler h=new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                contar();
            }
        };
        h.postDelayed(r, 1000);
    }

    protected void contarenotrohilohandlerpost()
    {
        final Handler h=new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        contar();
                    }
                },1000);
            }
        };
        h.post(r);
    }

    protected void contardeunoenuno()
    {
        contador=0;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(contaruno()){
                            Log.d("contardeunoenuno()","cancelando timertask ...");
                            cancel();
                        }
                    }
                });
            }
        }, 1000, 1000);
    }
    protected void contardeunoenunoyesperar()
    {
        //el semaforo esta cerrrado
        contador=0;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(contaruno())
                        {
                            semaforo.countDown(); //abrir el semaforo
                            Log.d("contardeun...yesperar()","cancelando await()...");
                            cancel();
                        };
                    }
                });
            }
        }, 1000, 1000);
        try {
            Log.d("contardeun...yesperar()","esperando await()...");
            semaforo.await(); //esperar hasta semaforo abierto
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
