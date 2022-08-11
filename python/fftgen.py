import struct
import os

import matplotlib.pyplot as plt
import numpy as np
import scipy.fftpack as fft
from scipy.interpolate import interp1d


def fftgen(wave):
    # plt.plot(wave)
    # plt.show()
    # inter = interp1d(np.arange(len(wave)), wave, kind='cubic')
    # wave = inter(np.arange(0, len(wave)-1, 0.5))

    f = fft.fftshift(fft.fft(wave))
    print(f)

    dat = open('../resources/fft.bin', 'wb')
    for i, e in enumerate(f):
        c = (i - len(f) / 2) / len(f)
        fftbin = abs(e) / len(f), c, np.angle(e)
        #print('amp: %f\t\tfreq: %f\t\tphase: %f' % (fftbin[0], fftbin[1], fftbin[2]))
        dat.write(struct.pack('>ddd', fftbin[0], fftbin[1], fftbin[2]))

        # fftbin = -e.imag / len(f), c, 0
        # #print('amp: %f\t\tfreq: %f\t\tphase: %f' % (fftbin[0], fftbin[1], fftbin[2]))
        # dat.write(struct.pack('>fff', fftbin[0], fftbin[1], fftbin[2]))

    dat.close()


if __name__ == '__main__':
    f = open('../resources/fft.path','rb')
    lst = []
    for i in range(os.path.getsize('../resources/fft.path')//8):
        ls = struct.unpack('<ff',f.read(8))
        lst.append(
            complex(ls[0],ls[1])
        )
    fftgen(lst)