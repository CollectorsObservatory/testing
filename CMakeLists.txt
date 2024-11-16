cmake_minimum_required(VERSION 3.29)
project(tp2-reseau-ip-CollectorsObservatory)

set(CMAKE_CXX_STANDARD 20)

add_executable(tp2-reseau-ip-CollectorsObservatory
        ContratException.cpp
        ContratException.h
        Routeur.cpp
        Routeur.h
        Reseau.cpp
        Reseau.h
        main.cpp
        Graphe.h
        Graphe.hpp)
