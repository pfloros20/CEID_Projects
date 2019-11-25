import pandas as pd
import numpy as np
import math
from sklearn import preprocessing
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras import regularizers


#Load data from csv file
data = pd.read_csv("california_housing_train.csv")

#A1
#Print labels and dimensions
print(data.columns)
print(data.shape)
#Get data as a matrix
values = data.values

#Normalize values in range [0,1]
#norm_values = (values - values.min(0))/(values.max(0) - values.min(0))
norm_values = preprocessing.scale(values)

#10 Fold Cross Validation
sets = np.split(norm_values[:,:8], 10)
label_sets = np.split(norm_values[:,8], 10)


def ten_fold_cv(nodes, learning_rate, epochs, momentum, loss, early_stop, output):

	file = open(output, "a")
	rmses = 0
	rrses = 0
	epoch_sum = 0

	for i in range(10):
		train_set = []
		train_labels = []
		test_set = sets[i]
		test_labels = label_sets[i]
		for j in range(10):
			if i != j:
				train_set.extend(sets[j])
				train_labels.extend(label_sets[j])
		train_set = np.array(train_set)
		train_labels = np.array(train_labels)

		model = keras.Sequential([
			layers.Dense(nodes, kernel_regularizer=regularizers.l2(loss), activation=tf.nn.relu, input_shape=[8,]),
			layers.Dense(1)
			])

		optimizer = tf.keras.optimizers.SGD(lr= learning_rate, momentum= momentum)

		model.compile(loss='mean_squared_error', optimizer=optimizer, metrics=['mean_squared_error'])

		#model.summary()

		# The patience parameter is the amount of epochs to check for improvement
		early_stop = keras.callbacks.EarlyStopping(monitor='val_loss', patience=10)
		print("Hidden Layer nodes: " + str(nodes))
		print("Training...")
		if early_stop == 1:
			model.fit(
			  train_set, train_labels, use_multiprocessing = True,
			  epochs=epochs, validation_split = 0.2, verbose=0, batch_size=1000,
			  callbacks=[early_stop])
		else:
			model.fit(
			  train_set, train_labels, use_multiprocessing = True,
			  epochs=epochs, validation_split = 0.2, verbose=0, batch_size=1000)

		# if early_stopping_monitor.stopped_epoch != 0:
		# 	epoch_sum+=early_stopping_monitor.stopped_epoch
		# else:
			epoch_sum+=epochs


		print("Evaluating...")
		curr_rmse = math.sqrt(model.evaluate(test_set, test_labels, use_multiprocessing= True, verbose=0)[0])
		rmses+= curr_rmse

		test_predictions = model.predict(test_set)
		stddev = np.std(test_predictions)
		print(stddev)
		rrses+=curr_rmse/stddev


	mean_rmse = rmses/10
	mean_rrse = rrses/10
	mean_epochs = epoch_sum/10
	print("Nodes: "+ str(nodes) + " RMSE: " + str(mean_rmse) + " RRSE%: " + str(mean_rrse*100) + "%")
	print("Writing results to file...")
	file.write("Nodes: "+ str(nodes) + " Learning rate: " + str(learning_rate) + " Epochs: " + str(mean_epochs) 
		+ " Momentum: " + str(momentum) + " Loss: " + str(loss) + " RMSE: " + str(mean_rmse) + " RRSE%: " + str(mean_rrse*100)+"%\n")
	file.flush()

	file.close();

#A2
nodes = [8, 5, 9]
for node in nodes:
	ten_fold_cv(node, 0.01, 500, 0.0, 0.0, 1, "A2s.txt")

#A3
nodes = 8
epochs = [100, 200, 500, 1000, 2000]
for epoch in epochs:
	ten_fold_cv(nodes, 0.1, epoch, 0.1, 0.0, 0, "A3s.txt")
	ten_fold_cv(nodes, 0.08, epoch, 0.2, 0.0, 0, "A3s.txt")
	ten_fold_cv(nodes, 0.05, epoch, 0.6, 0.0, 0, "A3s.txt")
	ten_fold_cv(nodes, 0.01, epoch, 1.0, 0.0, 0, "A3s.txt")

#A4
nodes = 8
learning_rate = 0.08
momentum = 0.2
loss_rates = [0.1, 0.5, 0.9]
epoch = 200
for r in loss_rates:
	ten_fold_cv(nodes, learning_rate, epoch, momentum, r, 1, "A4s.txt")