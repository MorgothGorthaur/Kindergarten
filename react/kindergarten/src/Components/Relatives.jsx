import {useState, useEffect} from "react";
import {Button, Modal, ModalFooter} from "react-bootstrap";
import RelativesService from "../API/RelativesService";
import RelativeListItem from "./RelativeLIstItem";
import RelativeForm from "./RelativeForm";

function Relatives({tokens, setTokens, kidId}) {
    const [relatives, setRelatives] = useState([]);
    const [showForm, setShowForm] = useState(false);
    useEffect(() => {
        async function fetchRelatives() {
            RelativesService.get(tokens, setTokens, kidId).then((data) => {
                if (data.debugMessage) alert(data.debugMessage);
                else setRelatives(data);
            });
        }

        fetchRelatives();
    }, []);

    return (
        <div>
            {relatives.length === 0 ? (
                <p>Relatives not found.</p>
            ) : (
                <div className="relatives-list-container">
                    <h3>All Relatives</h3>
                    <ul className="relatives-list">
                        {relatives.map((relative) => (
                            <RelativeListItem
                                key={relative.id}
                                relative={relative}
                                tokens={tokens}
                                setTokens={setTokens}
                                relatives={relatives}
                                setRelatives={setRelatives}
                                kidId={kidId}
                            />
                        ))}
                    </ul>
                </div>
            )}
            <div className="add-relative-btn">
                <Button variant="primary" onClick={() => setShowForm(true)}>Add Relative</Button>
                <Modal show={showForm} onHide={setShowForm}>
                    <RelativeForm tokens={tokens} setTokens={setTokens} relatives={relatives}
                                  setRelatives={setRelatives} setShowForm={setShowForm} kidId={kidId}/>
                </Modal>
            </div>
        </div>
    );
}

export default Relatives;