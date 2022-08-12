# This file converts the output of https://github.com/TheKOG/Fourier_Draw to fft bins

from fftgen import fftgen
from sys import argv

file = open(argv[1])

lines = int(file.readline())
points = []

for i in range(lines):
    line = file.readline().split(' ')
    c = complex(int(line[0]), int(line[1]))
    points.append(c)

print(points)
fftgen(points)