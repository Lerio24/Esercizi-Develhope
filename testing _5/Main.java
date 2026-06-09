const stringaIniziale = "2023-03-01T13:00:00Z";
const data = new Date(stringaIniziale);

data.setFullYear(data.getFullYear() + 1);
data.setMonth(data.getMonth() - 1);
data.setDate(data.getDate() + 7);

const risultato = data.toLocaleString('it-IT', { timeZone: 'Europe/Rome' });

console.log(risultato); 
// Output: 8/2/2024, 14:00:00