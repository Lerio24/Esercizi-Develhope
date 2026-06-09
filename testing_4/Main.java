const { test } = require('node:test');
const assert = require('node:assert');

// 1. LOGICA DELL'ESERCIZIO
function analizzaData(stringaIso) {
  const data = new Date(stringaIso);

  const risultati = {
    anno: data.getFullYear(),
    mese: data.getMonth() + 1, // +1 perché i mesi in JS vanno da 0 a 11
    giorno: data.getDate(),
    giornoSettimana: data.getDay() // 0 = Domenica, 1 = Lunedì, ecc.
  };

  console.log("Anno:", risultati.anno);
  console.log("Mese:", risultati.mese);
  console.log("Giorno:", risultati.giorno);
  console.log("Giorno della settimana:", risultati.giornoSettimana);

  return risultati;
}

// Esecuzione sulla console
const stringaIniziale = "2023-03-01T13:00:00Z";
analizzaData(stringaIniziale);


// 2. TEST PER L'ESERCIZIO
test('Verifica estrazione componenti della data', () => {
  const input = "2023-03-01T13:00:00Z";
  const datiEstratti = analizzaData(input);

  assert.strictEqual(datiEstratti.anno, 2023);
  assert.strictEqual(datiEstratti.mese, 3);
  assert.strictEqual(datiEstratti.giorno, 1);
  assert.strictEqual(datiEstratti.giornoSettimana, 3); // Il 1 Marzo 2023 era un Mercoledì
});