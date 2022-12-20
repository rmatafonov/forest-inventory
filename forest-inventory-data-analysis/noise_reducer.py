from scipy.io import wavfile
import noisereduce as nr


# load data
rate, data = wavfile.read("C:\\work\\hackathon\\public_data_task_2\\public_data\\train_audio.wav")
# perform noise reduction
reduced_noise = nr.reduce_noise(y=data, sr=rate, n_std_thresh_stationary=1.7)
wavfile.write("C:\\work\\hackathon\\mywav_reduced_noise1text.wav", rate, reduced_noise)
