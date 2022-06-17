from __future__ import division, print_function
# Coordinates are given as points in the complex plane
from svgpathtools import Path, Line, QuadraticBezier, CubicBezier, Arc
import numpy as np

seg1 = CubicBezier(
    300 + 100j, 100 + 100j, 200 + 200j,
    200 + 300j)  # A cubic beginning at (300, 100) and ending at (200, 300)
seg2 = Line(200 + 300j, 250 +
            350j)  # A line beginning at (200, 300) and ending at (250, 350)
path = Path(seg1, seg2)  # A path traversing the cubic and then the line

from svgpathtools import parse_path
from svgpathtools import svg2paths, wsvg

paths, attributes = svg2paths(
    'C:/Users/havey/Desktop/绘图-3.svg')

from fftgen import fftgen

print(paths)

lst = []

precision = 500
for path in paths:
    for i in range(precision):
        lst.append(path.point(i / precision))

lst = np.array(lst)/5
# a, b = np.min(np.abs(lst)), np.max(np.abs(lst))

# norm = (lst+a)  / (b-a)
# print(norm)
fftgen(lst)