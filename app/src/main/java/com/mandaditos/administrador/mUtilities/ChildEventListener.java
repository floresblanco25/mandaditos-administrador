package com.mandaditos.administrador.mUtilities;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import com.mandaditos.administrador.mUtilities.*;

import android.support.v4.app.TaskStackBuilder;
import com.mandaditos.administrador.R;
import com.mandaditos.administrador.mUtilities.ChildEventListener;

public class ChildEventListener extends Service
 {

	FirebaseAuth auth;
	NotificationCompat.Builder builder;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent,  int flags, int startId) {
		retrivemsg();
		return START_STICKY;
	}

	public void retrivemsg()
	{
		DatabaseReference mdb= FirebaseDatabase.getInstance().getReference("Ordenes");
		mdb.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
				@Override
				public void onChildAdded(DataSnapshot dataSnapshot, String keyName) {

						Intent i = new Intent(ChildEventListener.this,Home.class);
					String empresa = dataSnapshot.child("empresaDePartida").getValue().toString();
					showNotification(ChildEventListener.this, "Nueva orden "+keyName, empresa, i);
				}

				@Override
				public void onChildChanged(DataSnapshot dataSnapshot, String keyName) {
					Intent i = new Intent(ChildEventListener.this,Home.class);
					String empresa = dataSnapshot.child("empresaDePartida").getValue().toString();
					String cliente = dataSnapshot.child("clienteDeDestino").getValue().toString();
					String instrucciones = dataSnapshot.child("instrucciones").getValue().toString();
					showNotification(ChildEventListener.this, "Orden editada "+keyName, empresa + " " + cliente+" "+instrucciones, i);
					
				}

				@Override
				public void onChildRemoved(DataSnapshot dataSnapshot) {
				}

				@Override
				public void onChildMoved(DataSnapshot dataSnapshot, String s) {

				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
	}
	public void showNotification(Context context, String title, String body, Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		int notificationId = 1;
		String channelId = "channel-01";
		String channelName = "Channel Name";
		int importance = NotificationManager.IMPORTANCE_MAX;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			NotificationChannel mChannel = new NotificationChannel(
                channelId, channelName, importance);
			notificationManager.createNotificationChannel(mChannel);
		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(body);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
		);
		mBuilder.setContentIntent(resultPendingIntent);

		notificationManager.notify(notificationId, mBuilder.build());
	}
	}
