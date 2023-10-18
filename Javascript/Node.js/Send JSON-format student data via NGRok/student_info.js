const http = require('http');

const students = [
    { id: 11111, name: 'Bruce Lee', score: 84 },
    { id: 22222, name: 'Jackie Chen', score: 93 },
    { id: 33333, name: 'Jet Li', score: 88 },
];

const server = http.createServer((req, res) => {
    if (req.url.startsWith('/api/score?student_id=')) {
        const studentId = parseInt(req.url.split('=')[1]);
        const student = students.find(student => student.id === studentId);

        if (student) {
            res.writeHead(200, { 'Content-Type': 'application/json' });
            res.end(JSON.stringify(student));
        } else {
            res.writeHead(404, { 'Content-Type': 'text/plain' });
            res.end('Student not found');
        }
    } else {
        res.writeHead(404, { 'Content-Type': 'text/plain' });
        res.end('Invalid URL');
    }
});

const PORT = 8000;
server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
