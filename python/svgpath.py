from __future__ import division, print_function
# Coordinates are given as points in the complex plane
import numpy as np
from sys import argv

from svgpathtools import svg2paths

paths, attributes = svg2paths(argv[1])

from fftgen import fftgen

print(paths)

lst = []

precision = 1000
for path in paths:
    for i in range(precision):
        lst.append(path.point(i / precision))

lst = np.array(lst) / 5
# a, b = np.min(np.abs(lst)), np.max(np.abs(lst))

# norm = (lst+a)  / (b-a)
# print(norm)
fftgen(lst)