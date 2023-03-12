import {Button, Modal} from "react-bootstrap";
import {useState} from "react";
import KidsService from "../API/KidsService";
import KidBrothersAndSisters from "./KidBrothersAndSisters";
import ChildForm from "./ChildForm";


function KidListItem({ kid, tokens, setTokens, kids, setKids}) {
    const [addChildForm, setAddChildForm] = useState(false);
    const [updateChildForm, setUpdateChildForm] = useState(false);
    const [brothersAndSisters, setBrothersAndSisters] = useState(false);
    const handleDelete = (id) => {
        KidsService.delete(id, tokens, setTokens).then((data) => {
            console.log(data);
            if (!data) {
                setKids([...kids.filter((k) => k.id !== id)]);
            } else alert(data.debugMessage);
        });
    };
    return (
        <div>
            <li className="kids-list-item" key={kid.id}>
                <span className="kid-name">{kid.name}</span>
                <span className="birth-year">({kid.birthYear})</span>
                <div className="button-container">
                    <Button variant="primary" onClick={() => setUpdateChildForm(true)}>update</Button>
                    <Button variant="danger"
                            onClick={() => handleDelete(kid.id)}>delete</Button>
                    <Button variant="primary" onClick={() => setBrothersAndSisters()}>brothers and sisters</Button>
                </div>
            </li>
            <Modal show={brothersAndSisters} onHide={setBrothersAndSisters}>
                <KidBrothersAndSisters id={kid.id} tokens={tokens} setTokens={setTokens}/>
            </Modal>
            <Modal show={updateChildForm} onHide={setUpdateChildForm}>
                <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens}
                           setShowForm={setUpdateChildForm} child={kid}/>
            </Modal>
        </div>
    );
}

export default KidListItem;