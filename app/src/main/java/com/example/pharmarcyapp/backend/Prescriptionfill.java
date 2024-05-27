
package com.example.pharmarcyapp.backend;

// File: SignUpWorkere.java

        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;

        import com.example.pharmarcyapp.Doctors.ViewPrescriptions.Presidcard;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class  Prescriptionfill extends AsyncTask<Void, Void, String> {
    private String url;
    private String data;
    private boolean isPost;
    private Context context;

    public Prescriptionfill(Context context, String url, String data, boolean isPost) {
        this.url = url;
        this.data = data;
        this.isPost = isPost;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder response = new StringBuilder();
        try {
            // Create URL object
            URL url = new URL(this.url);

            // Create connection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (isPost) {
                // Set request method for POST
                connection.setRequestMethod("POST");

                // Enable output and set output stream
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();

                // Write data to output stream
                os.write(this.data.getBytes());
                os.flush();
                os.close();
            } else {
                // Set request method for GET
                connection.setRequestMethod("GET");
            }

            // Get response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            // Close connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the response from the server
        System.out.println("Response: " + result);
        Intent intent = new Intent(context, Presidcard.class);
        context.startActivity(intent);







//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    }
}
