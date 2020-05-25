package com.mandaditos.administrador.mUtilities;

import android.app.*;
import android.content.*;
import android.util.*;
import android.widget.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;

import com.mandaditos.administrador.R;
import android.support.annotation.*;
import android.os.*;
import android.content.pm.*;
import android.support.v4.app.*;

public class Servicio extends Service
 {

	FirebaseDatabase database = FirebaseDatabase.getInstance();
	DatabaseReference referenciaPersonaAgregada = database.getReference("Ordenes");
	String datosFirebase = "";
	String datosFirebaseCopia = "";
	@Override
	public void onCreate() {
		super.onCreate();


	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		String manufacturer = "xiaomi";
		if(manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
			//this will open auto start screen where user can enable permission for your app
			Intent intent2 = new Intent();
			intent2.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent2);
		}

		referenciaPersonaAgregada.addValueEventListener(new ValueEventListener() {

				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					//Guardo en un HashMap todos los datos de la base de datos valgase la redundancia de Firebase.
					datosFirebase = String.valueOf(dataSnapshot.getValue());

					if (datosFirebase==null){
						datosFirebase="";
					}

					//Si el clon esta vacio (Obviamente estara vacio la primera vez) haz una copia de lo primero que ha cogido de la base de datos.
					if (datosFirebaseCopia.isEmpty()) {

						datosFirebaseCopia = datosFirebase;

					}
					//Si ambas son iguales, nada ha cambiado ( La primera vez obviamente nada ha cambiado)
					if (datosFirebase.equals(datosFirebaseCopia)) {
						Log.d("TEST", "Hey hola, ¿Que has hecho? , vengo de revisar la base de datos ¿Y que crees? , no hay nada nuevo... sin cambios, no cambia, no madura,no crece la" +
							  "base de datos.");
					}
					//Pero si la segunda vez que cheque si son iguales y no lo son, es que la base de datos ha cambiado
					else {
						//Ahora si, echame la notificaciooon!
						//Notificación , nada sorprendente.
						NotificationCompat.Builder mBuilder;
						NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
						int icono = R.drawable.ic_launcher;
						Intent intent = new Intent(getBaseContext(), Home.class);
						PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);

						mBuilder = new NotificationCompat.Builder(getApplicationContext())
							.setContentIntent(pendingIntent)
							.setSmallIcon(icono)
							.setContentTitle("Firebase")
							.setContentText("Oyes, acaba de cambiar la base de datos , te aviso nadamas por si querias saber ¿no?")
							.setVibrate(new long[]{100, 250, 100, 500})
							.setAutoCancel(true);

						mNotifyMgr.notify(1, mBuilder.build());
						//-------------Fin del codigo notificacion.

						datosFirebaseCopia = datosFirebase;
					}

					//Los valores de la base de datos, digo, por si los quieres ver , ¿Para que? , pues para nada, te ayuda a comprender un poco que se esta haciendo viendo algo graficamente, yo digo, ¿No? .-. .
					Log.d("TEST", "Value is: " + datosFirebase);


				}
				@Override
				public void onCancelled(DatabaseError error) {
					// Failed to read value
					Log.w("TEST", "No funciono mi idea >.<", error.toException());
				}
			});

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onTaskRemoved(Intent rootIntent) {
		Log.e("FLAGX : ", ServiceInfo.FLAG_STOP_WITH_TASK + "");
		Intent restartServiceIntent = new Intent(getApplicationContext(),
												 this.getClass());
		restartServiceIntent.setPackage(getPackageName());

		PendingIntent restartServicePendingIntent = PendingIntent.getService(
            getApplicationContext(), 1, restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmService = (AlarmManager) getApplicationContext()
            .getSystemService(Context.ALARM_SERVICE);
		alarmService.set(AlarmManager.ELAPSED_REALTIME,
						 SystemClock.elapsedRealtime() + 1000,
						 restartServicePendingIntent);

		super.onTaskRemoved(rootIntent);
	}
	}
