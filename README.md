# DOCTOR CLIENT API for WOLFFGRAM: wolff_doctor

**USER MANUAL**

We have designed an application to monitor the Wolff Parkinson white syndrome. The application allows the patient to send signs and symptoms that are relevant for the diagnosis of the disease.

It will also allow him/her to send physiological parameters using the Bitalino device, to which the application is connected.

The Wolff Parkinson white syndrome is a disorder of the electrical system of the heart. About 40% of people who suffer from it do not present symptoms; some of them are an abnormally fast heartbeat, palpitations, shortness of breath, syncope.

The cause is unknown; some cases are due to a mutation which may be inherited. This mutation leads to an accessory electrical conduction pathway between the atria and the ventricles.

The diagnosis can be done when we observe in the electrocardiogram (ECG) a short PR interval and a delta wave. It is a type of pre-excitation syndrome.

This syndrome affects between the 0.1 and 0.3% in the population. The risk of death in those without symptoms is about 0.5% per year in children and 0.1% per year in adults. WPW is also associated with a very small risk of sudden death due to more dangerous heart rhythm disturbances.

Our aim with this project is to provide a way to detect this unknown disease in an easier way, facilitating the measuring of the ECG to the patient. He can do it at home, saving him visits to the hospital and allowing him to do the measuring more frequently, which is better to monitor the disorder and put him in the right treatment. It allows a constant following of the development of the disease in a more continuous way which is also better for reducing the symptoms.

The patient can also write down his symptoms at every moment he wants to record a new ECG, which also enables the doctor to have a continuous tracking of the state of the patient.

**WOLLGRAM APP USAGE**

With the Server being open, we can run the clients app. Here, **wolff\_doctor** usage is explained.

It will also be connected to the server; if the server is not connected the Doctor will not be able to connect, works the same way as the patient.


Once the server is open, the doctor can connect to it. When we first run the doctor, a welcome window will appear where the password is asked to the doctor. Before inserting it, the doctor must click on the settings button on the right, in order to introduce the IP address. After doing that, the doctor can now write the password (password=doctor by default) and press enter to have **access to all the information of the different patients**.


In this table the doctor can visualize the different patients and their personal data. By clicking on the button view, the medical history will all the recordings of that patient will pop up. An interesting and implemented function is that the doctor has a **Doctor comment option** , where the doctor can add information or give feedback to the patient based on the previous information; and also, the doctor can check the data of each ECG done by the patient.


From the medical history of the patient, the doctor can go back to the main menu in case he/she wants to check other patients and by pressing the logout button or by clicking on the x of the window, the doctor will close the program.

We assure the user a perfect connection to the server at any time, checking the connection between them. If the server fails, **we have considered these connection errors in order to manage them and inform him/her**.

When that happens, **the client (patient/doctor) will be notified, and information won&#39;t be updated until the connection is re-established** and the user logs in again.
