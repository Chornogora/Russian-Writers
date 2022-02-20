import collections
from functools import reduce
import matplotlib.pyplot as plt
import seaborn as sns


def draw_diagram(statistics, target_directory, file_name):
    display_data = preprocess_statistics(statistics)

    plt.figure(figsize=(10, 4.8))
    plt.barh(display_data['letters'], display_data['frequencies'], color='slateblue')
    plt.title(file_name, fontsize=27)
    plt.xlabel('Frequency, %', fontsize=19)
    sns.despine(left=True)
    plt.savefig(target_directory / (file_name + '.png'))


def preprocess_statistics(statistics):
    sorted_stat = collections.OrderedDict(sorted(statistics.items()))
    letters = list(sorted_stat.keys())
    letters.reverse()
    frequencies = list(sorted_stat.values())
    frequencies.reverse()

    total = reduce(lambda a, b: a + b, frequencies)
    for idx, val in enumerate(frequencies):
        frequencies[idx] = val / total * 100

    return {'letters': letters, 'frequencies': frequencies}
