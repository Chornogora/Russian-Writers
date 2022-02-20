import configparser
from typing import Final
from pathlib import Path

from diagrams import draw_diagram

config_file: Final = 'python.ini'


def process_statistics():
    config = read_config()
    output_folder_path = config['Settings']['output_folder']
    directories = Path(output_folder_path)
    for directory in directories.iterdir():
        process_directory(directory)
    return "text"


def read_config():
    config = configparser.ConfigParser()
    config.read(config_file)
    return config


def process_directory(directory):
    diagram_directory = directory.resolve() / "diagrams"
    if not diagram_directory.exists():
        diagram_directory.mkdir()
    directory_statistics = dict()
    for file in directory.iterdir():
        if file.is_file():
            statistics = process_file(file, diagram_directory)
            letters = list(statistics.keys())
            for letter in letters:
                if 'p' in directory_statistics:
                    directory_statistics[letter] += statistics[letter]
                else:
                    directory_statistics[letter] = statistics[letter]
    draw_diagram(directory_statistics, diagram_directory, diagram_directory.parent.name)


def process_file(file, target_directory):
    statistics = read_file(file)
    statistics = sort_by_frequency(statistics)
    draw_diagram(statistics, target_directory, file.name.split("_statistics")[0])
    return statistics


def read_file(file):
    result = dict()
    with open(file) as f:
        lines = f.readlines()
    for line in lines:
        statics = line.split()
        result[statics[0]] = int(statics[1])
    return result


def sort_by_frequency(statistics):
    return statistics
