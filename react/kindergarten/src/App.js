import './App.css';
import {Routes, Route} from 'react-router-dom';
import Teachers from "./Components/Teachers";
import {Button, Modal} from "react-bootstrap";
import AddTeacherForm from "./Components/AddTeacherForm";
import {useState} from "react";
import Home from './Components/Home';
function App() {
    const [showForm, setShowForm] = useState(false);
    return (
        <div className="App">
            <header className="App-header">
                <div className="nav-links">
                    <a href="/">main</a>
                    <a href="/teachers">teachers</a>
                    <a href="/home"> home </a>
                </div>
            </header>
            <Routes>
                <Route path="/" element={
                    <div>
                        <h1>Main Page</h1>
                        <Button onClick={() => setShowForm(true)}>add teacher</Button>
                        <Modal show={showForm} onHide={setShowForm}> <AddTeacherForm /></Modal>
                    </div>
                }/>
                <Route path="/teachers" element={<Teachers/>}/>
                <Route path="/home" element = {<Home/>} />
            </Routes>
        </div>
    );
}

export default App;