package techjini.com.myid3;

import android.Manifest;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.cmc.music.common.ID3WriteException;
import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.ImageData;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1001);
        modifymp3();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void modifymp3(){
        try {

            String pathdata = Environment.getExternalStorageDirectory().toString()+File.separator+"Download"+File.separator+"test.mp3";

            File src = new File(pathdata);

            InputStream is = new FileInputStream(src);


            System.out.println("src"+src.exists());
            MusicMetadataSet src_set = null;
            try {
                src_set = new MyID3().read(src);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // read metadata

            if (src_set == null) // perhaps no metadata
            {
                Log.i("NULL", "NULL");
            }
            else
            {

                MusicMetadata meta = new MusicMetadata("name");
                meta.setAlbum("Techjini");
                meta.setArtist("Arun");
                Drawable d = getDrawable(R.drawable.abc); // the drawable (Captain Obvious, to the rescue!!!)
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                ImageData imageData = new ImageData(bitmapdata,"image/jpeg","arun photo",1);

                meta.addPicture(imageData);
                try {
                    new MyID3().update(src, src_set, meta);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ID3WriteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }  // write updated metadata


                try{
                    IMusicMetadata metadata = src_set.getSimplified();
                    String artist = metadata.getArtist();
                    String album = metadata.getAlbum();
                    String song_title = metadata.getSongTitle();
                    Number track_number = metadata.getTrackNumber();
                    Log.i("artist", artist);
                    Log.i("album", album);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
