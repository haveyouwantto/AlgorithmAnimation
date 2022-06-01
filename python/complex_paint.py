import tkinter as tk
import fftgen
import struct


class Paint:
    def __init__(self):
        self.root = tk.Tk()
        self.root.bind('<B1-Motion>', self.paint)
        self.root.bind('<Return>', self.fft)
        self.root.geometry('500x500')
        self.canvas = tk.Canvas(self.root, width=500, height=500)
        self.canvas.pack()
        self.evt = None
        self.list = []
        self.root.mainloop()

    def paint(self, event):
        if self.evt is not None:
            self.canvas.create_line(self.evt.x, self.evt.y, event.x, event.y)
        self.list.append(complex(event.x / 500 - 0.5, event.y / 500 - 0.5))
        self.evt = event

    def fft(self, event):
        fftgen.fftgen(self.list)

        dat = open('../resources/path.bin', 'wb')
        for e in self.list:
            dat.write(struct.pack('<ff', e.real, e.imag))
        dat.close()

        self.list.clear()


Paint()