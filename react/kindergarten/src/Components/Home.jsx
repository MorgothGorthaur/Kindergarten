import {useState, useEffect} from "react";
import Login from "./Login";
import TeacherService from "../API/TeacherService";
import LoginService from "../API/LoginService";
import {Modal, Button} from "react-bootstrap";
import UpdateTeacherForm from "./UpdateTeacherForm";

function Home() {
    const [tokens, setTokens] = useState(null);
    const [showLoginForm, setShowLoginForm] = useState(true);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [teacher, setTeacher] = useState("");

    const handleLogin = (data) => {
        setTokens(data);
        setShowLoginForm(false);
    };

    useEffect(() => {
        if (tokens) {
            TeacherService.getTeacher(tokens.access_token).then((data) => {
                if (!data.hasError) {
                    setTeacher(data);
                }
            });
        }
    }, [tokens]);


    const handleDelete = () => {
        TeacherService.delete(tokens).then(data => {
            console.log(data);
            if (data.message === "authorization or authentication exception") {
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert(data.debugMessage)
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        TeacherService.delete(refresh);
                    }
                });
            }
        });
    };
    return (
        <div>
            {teacher ? (
                <div>
                    <div className="teacher-info">
                        <h3>Your Name: {teacher.name}</h3>
                        <h3>Your Phone: {teacher.phone}</h3>
                        <h3>Your Skype: {teacher.skype}</h3>
                    </div>
                    <div className="teacher-actions">
                        <Button variant="primary" onClick={() => setShowUpdateForm(true)}>update</Button>{" "}
                        <Button variant="danger" onClick={handleDelete}>delete</Button>
                        <Modal show={showUpdateForm} onHide={setShowUpdateForm}>
                            <UpdateTeacherForm teacher={teacher} tokens = {tokens} setTokens = {setTokens}/>
                        </Modal>
                    </div>
                </div>
            ) : tokens ? (
                <th>Loading teacher data...</th>
            ) : (
                <Modal show={showLoginForm} onHide={setShowLoginForm}> <Login setTokens={handleLogin}
                                                                              setModal={setShowLoginForm}/>
                </Modal>
            )}
        </div>
    );
}

export default Home;