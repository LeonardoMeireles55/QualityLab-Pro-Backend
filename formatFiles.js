const { exec } = require("child_process");
const fs = require("fs");
const path = require("path");

const root = "."; // Diretório raiz do projeto
const files = [];

function findJavaFiles(dir) {
    fs.readdirSync(dir).forEach((file) => {
        const fullPath = path.join(dir, file);
        if (fs.statSync(fullPath).isDirectory()) {
            findJavaFiles(fullPath);
        } else if (fullPath.endsWith(".java")) {
            files.push(fullPath);
        }
    });
}

findJavaFiles(root);

files.forEach((file) => {
    exec(`code ${file} --command editor.action.formatDocument --wait`, (err) => {
        if (err) console.error(`Error formatting ${file}: ${err}`);
    });
});
