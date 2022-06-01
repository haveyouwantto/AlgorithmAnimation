import struct

import matplotlib.pyplot as plt
import numpy as np
import scipy.fftpack as fft
from scipy.interpolate import interp1d
from scipy.signal import blackman, square


def fftgen(wave):
    # plt.plot(wave)
    # plt.show()
    inter = interp1d(np.arange(len(wave)), wave, kind='cubic')
    wave = inter(np.arange(0, len(wave)-1, 0.1))

    f = fft.fftshift(fft.fft(wave))
    print(f)

    dat = open('../resources/fft.bin', 'wb')
    for i, e in enumerate(f):
        c = (i - len(f) / 2) / len(f)
        fftbin = abs(e) / len(f), c, np.angle(e)
        #print('amp: %f\t\tfreq: %f\t\tphase: %f' % (fftbin[0], fftbin[1], fftbin[2]))
        dat.write(struct.pack('>fff', fftbin[0], fftbin[1], fftbin[2]))

        # fftbin = -e.imag / len(f), c, 0
        # #print('amp: %f\t\tfreq: %f\t\tphase: %f' % (fftbin[0], fftbin[1], fftbin[2]))
        # dat.write(struct.pack('>fff', fftbin[0], fftbin[1], fftbin[2]))

    dat.close()
