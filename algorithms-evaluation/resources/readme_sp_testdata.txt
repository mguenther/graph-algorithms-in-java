Beitrag von Stille 15. Dez 2008 18:18
Hallo,

die Testsets sind nun online. Da es permanent Probleme mit dem Webserver gibt, hier nochmal die Links:

http://zuseex.algo.informatik.tu-darmstadt.de/lehre/2008ws/graphalgo/mat/testsets/NYC.sp.gz
http://zuseex.algo.informatik.tu-darmstadt.de/lehre/2008ws/graphalgo/mat/testsets/COL.sp.gz
http://zuseex.algo.informatik.tu-darmstadt.de/lehre/2008ws/graphalgo/mat/testsets/FLA.sp.gz

Es handelt sich um Realdaten von Strassennetzwerken in den Vereinigten Staaten (NYC, Colorado und Florida).

Da ich ohnehin dieses Jahr nicht mehr dazu kommen werde, mir Ihre Lösungen anzusehen, und es vor Weihnachten
immer genug anderes zu tun gibt, verlängere ich die Abgabefrist auf Ende 2008. In welcher Zeitzone Sie sich am Ende
des Jahres - zumindest virtuell - befinden wollen, können Sie selbst bestimmen.

Um auch denjenigen Leuten die Chance zu geben diese Übung zu machen, die die Übung 3 nicht implementiert haben, bzw.
in deren Datenstrukturen sich immer noch Bugs befinden, erlaube ich ausnahmsweise die Benutzung der Datenstrukturen aus
JUNG (http://jung.sourceforge.net/) bzw. JGraphT (http://jgrapht.sourceforge.net/). Das gilt jedoch wohlgemerkt nur für die Datenstrukturen, nicht für die Algorithmen!

Mit einer Ausnahme, und zwar soll bei Benutzung von jgrapht eine zusätzliche Option implementiert werden, und zwar -j.
Diese soll den in JUNG bzw. JGraphT implementierten Dijkstra-Algorithmus aufrufen. Der Vergleichstest soll diese Variante
dann natürlich auch berücksichtigen.
Wolfgang Stille

--------------------------------------------------------------------------------

Beitrag von blowfish 16. Dez 2008 16:52
Hi,
ich habe ein paar Probleme mit den Testgraphen. Eine Kleinigkeit vielleicht, aber ich finde dennoch erwähnenswert: in der FLA.sp sind in der Datei das "m" und "n" im Header vertauscht. Ist zwar nicht schwer anzupassen, aber entspricht nicht der Spezifikation.
Zum Zweiten gibt es Probleme mit der Konsistenzbedingung zur zielgerichteten Suche (Folie 128, bzw Kapitel 4, Seite 24). Wir sollen als untere Schranke für den kürzesten Weg von u nach t die euklidische Distanz nehmen, was logisch klingt, weil wohl kein Weg kürzer sein kann. Die Konsistenzbedingung sagt aus:
b(ut)cuv+b(vt)
Für den Graphen in NYC.sp bekomme ich für u=0, v=236009 und t=200 die folgenden Werte:
b(ut)=365833
cuv=368
b(vt)=365455

Einfaches einsetzen ergibt:
cuv+b(vt)=368+365455=365823365833=b(ut)
Das verletzt die Konsistenzbedingung. Leider ist das offensichtlich kein Einzelfall. Ich hab mir eine Methode geschrieben, die einen Graphen auf solche Inkonsistenzen für gegebenen Knoten t durchsucht und es spuckt haufenweise Fehler aus, wenn ich es auf die gegebenen Graphen durchlaufen lasse. Ich runde die euklidische Distanz übrigens immer ab.

Hab ich einen Denkfehler, versteh ich die Folie falsch oder sind die Testgraphen tatsächlich inkonsistent?

--------------------------------------------------------------------------------

Beitrag von Stille 17. Dez 2008 00:38 

So, da bin ich wieder. Das Problem liegt darin, daß in den Testdatensätzen die Koordinaten als geographische Koordinaten im Dezimalminutenformat in der Form (Longitude, Latitude) angegeben sind, und man zur Distanzbestimmung nicht so ohne weiteres Pythagoras anwenden kann (wir leben nunmal nicht auf einer Scheibe). Die Distanzberechnung kann nach dem folgenden Schema erfolgen:

Code: Alles auswählen
    static double calculateDistance(double long1, double lat1, double long2, double lat2) {

            // conversion to radiant and standard M.m format
            lat1 = (lat1 * 2.0 * Math.PI) / 360.0 / 1000000; 
            long1 = (long1 * 2.0 * Math.PI) / 360.0 / 1000000;
            lat2 = (lat2 * 2.0 * Math.PI) / 360.0 / 1000000;
            long2 = (long2 * 2.0 * Math.PI) / 360.0 / 1000000;
          
            // earth parameters
            double a = 6378137.0;           // Earth Major Axis (WGS84)
            double b = 6356752.3142;        // Minor Axis
            double f = (a-b) / a;           // "Flattening"
            double e = 2.0*f - f*f;         // "Eccentricity"
           
            // calculate some aux values for first coordinate
            double beta  = (a / Math.sqrt( 1.0 - e * Math.sin( lat1 ) * Math.sin( lat1 )));
            double cos   = Math.cos( lat1 );
            double x   = beta * cos * Math.cos( long1 );
            double y   = beta * cos * Math.sin( long1 );
            double z   = beta * ( 1 - e ) * Math.sin( lat1 );
          
            // calculate some aux values for second coordinate w.r.t. first coordinate
            beta    = ( a / Math.sqrt( 1.0 -  e * Math.sin( lat2 ) * Math.sin( lat2 )));
            cos     = Math.cos( lat2 );
            x       -= (beta * cos * Math.cos( long2 ));
            y       -= (beta * cos * Math.sin( long2 ));
            z       -= (beta * (1 - e) * Math.sin( lat2 ));
          
            // calculate distance in meters
            double dist= Math.sqrt( (x*x) + (y*y) + (z*z) );
           
            // conversion to feet  (commented out )
            //dist *= 3.2808399;
            return dist;
          }



Bitte beachten Sie, daß die Distanzen auf den Kanten höchstwahrscheinlich in Fuß angegeben sind, und nicht in Metern. Sie erhalten die Luftlinienentfernung in Fuß, indem Sie oben die vorletzte Zeile entkommentieren.

Damit sollte es funktionieren. Sollte es wider Erwarten noch Probleme mit den Daten geben, melden Sie sich bitte.
Wolfgang Stille 
