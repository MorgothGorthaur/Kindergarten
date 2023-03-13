import {useEffect, useState} from "react";
import KidsService from "../API/KidsService";
import {Button, Modal} from "react-bootstrap";
import ChildForm from "./ChildForm";
import KidBrothersAndSisters from "./KidBrothersAndSisters";
import Relatives from "./Relatives";
import KidListItem from "./KidListItem";

function AllKids({tokens, setTokens}) {
    const [kids, setKids] = useState([]);
    const [addChildForm, setAddChildForm] = useState(false);

    const [selectedKid, setSelectedKid] = useState(null);
    const [relatives, setRelatives] = useState(false);
    useEffect(() => {
        async function fetchData() {
            const data = await KidsService.getAll(tokens, setTokens);
            setKids(data);
        }

        fetchData();
    }, []);




    function handleRelative(kid) {
        setSelectedKid(kid)
        setRelatives(true)
    }

    return (
        <div>
            {kids.length === 0 ? (
                <p>Kids not found</p>
            ) : (
                <div className="kids-list-container">
                    <h3>All kids</h3>
                    <ul className="kids-list">
                        {kids.map((kid) => (
                            <KidListItem kid={kid} tokens={tokens} setTokens={setTokens} kids={kids} setKids={setKids} />
                        ))}
                    </ul>
                </div>
            )}
            <div>
                <Button variant="primary" className="m-3" onClick={() => setAddChildForm(true)}>Add
                    Child</Button>
                <Modal show={addChildForm} onHide={setAddChildForm}>
                    <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens}
                               setShowForm={setAddChildForm}/>
                </Modal>
            </div>
        </div>
    );
}

export default AllKids;